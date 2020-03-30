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

import com.hindsite.experimental.pstfile.nodedatabase.block.BlockID;
import com.hindsite.experimental.pstfile.nodedatabase.enums.PageTypeName;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class BTreeIndexPage extends BTreePage {

    public static final int MaximumNumberOfEntries = 20; // 488 / 24

    public List<BTreeIndexEntry> IndexEntryList = new ArrayList<>();

    public BTreeIndexPage() {
        super();
    }

    public BTreeIndexPage(byte[] buffer) {
        super(buffer);
    }

    @Override
    public void PopulateEntries(byte[] buffer, byte numberOfEntries) {
        for (int index = 0; index < numberOfEntries; index++) {
            int offset = index * cbEnt;
            BTreeIndexEntry entry = new BTreeIndexEntry(buffer, offset);
            IndexEntryList.add(entry);
        }
    }

    @Override
    public int WriteEntries(byte[] buffer) {
        int offset = 0;
        for (BTreeIndexEntry entry : IndexEntryList) {
            entry.WriteBytes(buffer, offset);
            offset += cbEnt;
        }

        return IndexEntryList.size();
    }

    public int GetIndexOfEntryWithMatchingRange(long key) {
        int lastToMatchIndex = 0;
        for (int index = 1; index < IndexEntryList.size(); index++) {
            BTreeIndexEntry entry = IndexEntryList.get(index);
            // All the entries in the child BTPAGE .. have key values greater than or equal to this key value.
            if (key >= entry.btkey) {
                lastToMatchIndex = index;
            }
        }
        return lastToMatchIndex;
    }

    public int GetIndexOfBlockID(long blockID) {
        for (int index = 0; index < IndexEntryList.size(); index++) {
            if (IndexEntryList.get(index).BREF.bid.getValue() == blockID) {
                return index;
            }
        }
        return -1;
    }

    public int GetIndexOfEntry(long key) {
        for (int index = 0; index < IndexEntryList.size(); index++) {
            if (IndexEntryList.get(index).btkey == key) {
                return index;
            }
        }
        return -1;
    }

    public int GetSortedInsertIndex(BTreeIndexEntry entryToInsert) {
        long key = entryToInsert.btkey;

        int insertIndex = 0;
        while (insertIndex < IndexEntryList.size() && key > IndexEntryList.get(insertIndex).btkey) {
            insertIndex++;
        }
        return insertIndex;
    }

    private void InsertSorted(long btkey, BlockID blockID) {
        BTreeIndexEntry entry = new BTreeIndexEntry();
        entry.BREF.bid = blockID;
        InsertSorted(entry);
    }

    /// <returns>Insert index</returns>
    public int InsertSorted(BTreeIndexEntry entryToInsert) {
        int insertIndex = GetSortedInsertIndex(entryToInsert);
        IndexEntryList.add(insertIndex, entryToInsert);
        return insertIndex;
    }

    public BTreeIndexPage Split() {
        int newNodeStartIndex = IndexEntryList.size() / 2;
        BTreeIndexPage newPage = new BTreeIndexPage();
        // blockID will be given when the page will be added
        newPage.cEntMax = cEntMax;
        newPage.cbEnt = cbEnt;
        newPage.cLevel = cLevel;
        newPage.pageTrailer.ptype = pageTrailer.ptype;
        for (int index = newNodeStartIndex; index < IndexEntryList.size(); index++) {
            newPage.IndexEntryList.add(IndexEntryList.get(index));
        }

        IndexEntryList.removeAll(IndexEntryList.subList(newNodeStartIndex, IndexEntryList.size() - newNodeStartIndex));
        return newPage;
    }

    public static BTreeIndexPage GetEmptyIndexPage(PageTypeName pageType, int cLevel) {
        BTreeIndexPage page = new BTreeIndexPage();
        page.pageTrailer.ptype = pageType;
        page.cbEnt = BTreeIndexEntry.Length;
        page.cEntMax = MaximumNumberOfEntries;
        page.cLevel = (byte) cLevel;
        return page;
    }

    // the key of the first entry in the page
    public long getPageKey() {
        return IndexEntryList.get(0).btkey;
    }
}
