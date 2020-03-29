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
package com.hindsite.experimental.pstfile.nodedatabase.block;

import com.hindsite.experimental.pstfile.nodedatabase.datastore.DataBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class BufferedBlockStore {

    private PSTFile m_file;

    // We use the block buffer to store cached and modified blocks
    // besides caching, the main purpose of this buffer is to store the modifications to the blocks,
    // this way they could be written to the PST file later in a single transaction.
    private Map<Long, Block> m_blockBuffer = new HashMap<>();

    // The NDB is immutable, we allocate blocks for both new blocks and modified blocks,
    // for modified blocks, we unallocate the original blocks (using m_blocksToFree).
    private List<Long> m_blocksToWrite = new ArrayList<>();
    private List<Long> m_blocksToFree = new ArrayList<>();

    protected BufferedBlockStore(PSTFile file) {
        m_file = file;
    }

    protected Block GetBlock(BlockID blockID) {
        return GetBlock(blockID.getValue());
    }

    /// <summary>
    /// Will get a block from the buffer,
    /// A cloned copy of the block will be returned
    /// </summary>
    protected Block GetBlock(long blockID) throws CloneNotSupportedException {
        if (m_blockBuffer.containsKey(blockID)) {
            return m_blockBuffer.get(blockID).Clone();
        } else {
            Block block = m_file.FindBlockByBlockID(blockID);
            m_blockBuffer.put(blockID, block);
            return block.Clone();
        }
    }

    protected boolean IsBlockPendingWrite(Block block) {
        return IsBlockPendingWrite(block.getBlockID());
    }

    protected boolean IsBlockPendingWrite(BlockID blockID) {
        return m_blocksToWrite.contains(blockID.getValue());
    }

    /// <param name="block">BlockID might be updated</param>
    public void UpdateBlock(Block block) throws Exception {
        if (block.getTotalLength() > Block.MaximumLength) {
            throw new Exception("Invalid block length");
        }

        if (IsBlockPendingWrite(block)) {
            // the block we wish to replace is already pending write
            // we just need to update the buffer:
            m_blockBuffer.put(block.getBlockID().getValue(), block);
        } else {
            // we need to mark the old block for freeing, and add the new block to the buffer
            DeleteBlock(block);
            boolean isInternal = block.getBlockID().isInternal();
            block.setBlockID(m_file.getHeader().AllocateNextBlockID());
            block.getBlockID().setInternal(isInternal);
            m_blockBuffer.put(block.getBlockID().getValue(), block);
            m_blocksToWrite.add(block.getBlockID().getValue());
        }
    }

    /// <param name="block">will be assigned a new BlockID</param>
    public void AddBlock(Block block) throws Exception {
        if (block.getTotalLength() > Block.MaximumLength) {
            throw new Exception("Invalid block length");
        }

        block.setBlockID(m_file.Header.AllocateNextBlockID());
        block.getBlockID().setInternal(!(block instanceof DataBlock));
        m_blockBuffer.put(block.getBlockID().getValue(), block);
        m_blocksToWrite.add(block.getBlockID().getValue());
    }

    public void DeleteBlock(Block block) {
        long blockID = block.getBlockID().getValue();

        if (m_blockBuffer.containsKey(blockID)) {
            // remove the old block from the cache
            m_blockBuffer.remove(blockID);
        }

        // no need to free a block that has not been written yet
        if (IsBlockPendingWrite(block)) {
            m_blocksToWrite.remove(blockID);
        } else {
            m_blocksToFree.add(blockID);
        }
    }

    /// <summary>
    /// The caller must update its reference to point to the new root
    /// </summary>
    public void SaveChanges() {
        for (long blockID : m_blocksToWrite) {
            Block block = m_blockBuffer.get(blockID);
            long offset = AllocationHelper.AllocateSpaceForBlock(m_file, block.TotalLength);
            block.WriteToStream(m_file.BaseStream, offset);
            m_file.getBlockBTree().InsertBlockEntry(block.BlockID, offset, block.DataLength);
        }

        for (long blockID : m_blocksToFree) {
            BlockBTreeEntry entry = m_file.FindBlockEntryByBlockID(blockID);
            entry.cRef--;
            // Any leaf BBT entry that points to a BID holds a reference count to it.
            if (entry.cRef == 1) {
                // we can mark the allocation to be freed and delete the entry,
                // We should not free the allocation until the BBT is committed.
                m_file.MarkAllocationToBeFreed(entry.BREF.ib, Block.GetTotalBlockLength(entry.cb));
                m_file.BlockBTree.DeleteBlockEntry(entry.BREF.bid);
            } else {
                m_file.BlockBTree.UpdateBlockEntry(entry.BREF.bid, entry.cRef);
            }
        }

        m_blocksToWrite.Clear();
        m_blocksToFree.Clear();
    }

    public PSTFile getFile() {
        return m_file;
    }
}
}
