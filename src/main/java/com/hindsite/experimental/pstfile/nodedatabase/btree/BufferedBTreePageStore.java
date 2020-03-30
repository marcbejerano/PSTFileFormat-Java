/*
 * Copyright © 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license
 * document, but changing it is not allowed.
 *
 * This version of the GNU Lesser General Public License incorporates the terms
 * and conditions of version 3 of the GNU General Public License, supplemented
 * by the additional permissions listed below.
 */
package com.hindsite.experimental.pstfile.nodedatabase.btree;

import com.hindsite.experimental.PSTFile;
import com.hindsite.experimental.pstfile.nodedatabase.block.BlockRef;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
class BufferedBTreePageStore {

    private PSTFile m_file;

    // We use the page buffer to store cached and modified BTree pages
    // besides caching, the main purpose of this buffer is to store the modifications to the BTree,
    // this way they could be written to the PST file later in a single transaction.
    private Map<Long, BTreePage> m_pageBuffer = new HashMap<>();

    // The NDB is immutable, we allocate pages for both new pages and modified pages,
    // for modified pages, we unallocate the original pages (using m_pagesToFree).
    private List<Long> m_pagesToWrite = new ArrayList<>();
    private List<Long> m_offsetsToFree = new ArrayList<>();

    public BufferedBTreePageStore(PSTFile file) {
        m_file = file;
    }

    public BTreePage GetPage(BlockRef blockRef) {
        long blockID = blockRef.bid.getValue();

        if (m_pageBuffer.containsKey(blockID)) {
            return m_pageBuffer.get(blockID);
        } else {
            BTreePage page = BTreePage.ReadFromStream(m_file.getBaseStream(), blockRef);
            m_pageBuffer.put(blockRef.bid.getValue(), page);
            return page;
        }
    }

    public boolean IsPagePendingWrite(BTreePage page) {
        return IsPagePendingWrite(page.getBlockID().getValue());
    }

    public boolean IsPagePendingWrite(long blockID) {
        return m_pagesToWrite.contains(blockID);
    }

    public void UpdatePage(BTreePage page) {
        if (IsPagePendingWrite(page)) {
            m_pageBuffer.put(page.getBlockID().getValue(), page);
        } else {
            long oldBlockID = page.getBlockID().getValue();
            if (m_pageBuffer.containsKey(oldBlockID)) {
                // remove the old block from the buffer
                m_pageBuffer.remove(oldBlockID);
            }

            m_offsetsToFree.add(page.Offset);

            page.setBlockID(m_file.getHeader().AllocateNextPageBlockID());

            m_pageBuffer.put(page.getBlockID().getValue(), page);
            m_pagesToWrite.add(page.getBlockID().getValue());
        }
    }

    public void AddPage(BTreePage page) {
        // It is not clear whether new BTree pages should be marked as internal or not
        // Outlook sometimes chooses to mark them as internal, and sometimes not (for both BBT and NBT)
        page.setBlockID(m_file.getHeader().AllocateNextPageBlockID());

        m_pageBuffer.put(page.getBlockID().getValue(), page);
        m_pagesToWrite.add(page.getBlockID().getValue());
    }

    public void DeletePage(BTreePage page) {
        long blockID = page.getBlockID().getValue();

        if (m_pageBuffer.containsKey(blockID)) {
            // remove the old block from the cache
            m_pageBuffer.remove(blockID);
        }

        // no need to free a block that has not been written yet
        if (IsPagePendingWrite(page)) {
            m_pagesToWrite.remove(blockID);
        } else {
            m_offsetsToFree.add(page.Offset);
        }
    }

    public void SaveChanges() throws IOException {
        List<BTreePage> pages = new ArrayList<>();
        for (BTreePage page : m_pageBuffer.values()) {
            if (m_pagesToWrite.contains(page.getBlockID().getValue())) {
                pages.add(page);
            }
        }
        SortByCLevel(pages, false);

        // we now have the pages in ascending cLevel order (i.e. leaves first)
        // we need them this way because we need to update the offset from old pages to new pages
        // so we must write children first
        // for new blocks:
        Map<Long, Long> blockToOffset = new HashMap<>();
        for (BTreePage page : pages) {
            if (page instanceof BTreeIndexPage) // page.cLevel > 0
            {
                // parent page, we may need to update references
                BTreeIndexPage indexPage = (BTreeIndexPage) page;
                for (int index = 0; index < indexPage.IndexEntryList.size(); index++) {
                    long childBlockID = indexPage.IndexEntryList.get(index).BREF.bid.getValue();

                    if (indexPage.IndexEntryList.get(index).BREF.ib == 0) // reference to a new / updated block
                    {
                        long blockOffset = blockToOffset.get(childBlockID);
                        indexPage.IndexEntryList.get(index).BREF.ib = blockOffset;
                    }
                }
            }

            long newPageAllocationOffset = AllocationHelper.AllocateSpaceForPage(m_file);
            blockToOffset.put(page.getBlockID().getValue(), newPageAllocationOffset);
            page.Offset = (long) newPageAllocationOffset; // we may need this later
            byte[] pageBytes = page.GetBytes((long) newPageAllocationOffset);
            //m_file.BaseStream.Seek(newPageAllocationOffset, SeekOrigin.Begin);
            m_file.getBaseStream().write(pageBytes, 0, pageBytes.length);
        }

        // free all the marked pages
        for (long offset : m_offsetsToFree) {
            AllocationHelper.FreePageAllocation(m_file, (long) offset);
        }

        m_pagesToWrite.clear();
        m_offsetsToFree.clear();
    }

    public void SortByCLevel(List<BTreePage> pages, boolean descendingOrder) {
        pages.sort((x, y) -> {
            return CompareByCLevel(x, y);
        });
        if (descendingOrder) {
            pages.Reverse();
        }
    }

    public PSTFile getFile() {
        return m_file;
    }

    private static int CompareByCLevel(BTreePage x, BTreePage y) {
        if (x.cLevel > y.cLevel) {
            return 1;
        } else if (x.cLevel < y.cLevel) {
            return -1;
        } else {
            return 0;
        }
    }
}
