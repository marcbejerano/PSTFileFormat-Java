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

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class BufferedBlockStore {
        private PSTFile m_file;
        
        // We use the block buffer to store cached and modified blocks
        // besides caching, the main purpose of this buffer is to store the modifications to the blocks,
        // this way they could be written to the PST file later in a single transaction.
        private Dictionary<ulong, Block> m_blockBuffer = new Dictionary<ulong, Block>();

        // The NDB is immutable, we allocate blocks for both new blocks and modified blocks,
        // for modified blocks, we unallocate the original blocks (using m_blocksToFree).
        private List<ulong> m_blocksToWrite = new List<ulong>();
        private List<ulong> m_blocksToFree = new List<ulong>();

        protected BufferedBlockStore(PSTFile file)
        {
            m_file = file;
        }

        protected Block GetBlock(BlockID blockID)
        {
            return GetBlock(blockID.Value);
        }

        /// <summary>
        /// Will get a block from the buffer,
        /// A cloned copy of the block will be returned
        /// </summary>
        protected Block GetBlock(ulong blockID)
        {
            if (m_blockBuffer.ContainsKey(blockID))
            {
                return m_blockBuffer[blockID].Clone();
            }
            else
            {
                Block block = m_file.FindBlockByBlockID(blockID);
                m_blockBuffer.Add(blockID, block);
                return block.Clone();
            }
        }

        protected bool IsBlockPendingWrite(Block block)
        {
            return IsBlockPendingWrite(block.BlockID);
        }

        protected bool IsBlockPendingWrite(BlockID blockID)
        {
            return m_blocksToWrite.Contains(blockID.Value);
        }

        /// <param name="block">BlockID might be updated</param>
        public void UpdateBlock(Block block)
        {
            if (block.TotalLength > Block.MaximumLength)
            {
                throw new Exception("Invalid block length");
            }

            if (IsBlockPendingWrite(block))
            {
                // the block we wish to replace is already pending write
                // we just need to update the buffer:
                m_blockBuffer[block.BlockID.Value] = block;
            }
            else
            {
                // we need to mark the old block for freeing, and add the new block to the buffer
                DeleteBlock(block);
                bool isInternal = block.BlockID.Internal;
                block.BlockID = m_file.Header.AllocateNextBlockID();
                block.BlockID.Internal = isInternal;
                m_blockBuffer.Add(block.BlockID.Value, block);
                m_blocksToWrite.Add(block.BlockID.Value);
            }
        }

        /// <param name="block">will be assigned a new BlockID</param>
        public void AddBlock(Block block) throws Exception
        {
            if (block.getTotalLength() > Block.MaximumLength)
            {
                throw new Exception("Invalid block length");
            }

            block.setBlockID(m_file.Header.AllocateNextBlockID());
            block.getBlockID().setInternal(!(block instanceof DataBlock));
            m_blockBuffer.add(block.getBlockID().getValue(), block);
            m_blocksToWrite.add(block.getBlockID().getValue());
        }

        public void DeleteBlock(Block block)
        {
            long blockID = block.getBlockID().getValue();
            
            if (m_blockBuffer.ContainsKey(blockID))
            {
                // remove the old block from the cache
                m_blockBuffer.Remove(blockID);
            }

            // no need to free a block that has not been written yet
            if (IsBlockPendingWrite(block))
            {
                m_blocksToWrite.Remove(blockID);
            }
            else
            {
                m_blocksToFree.Add(blockID);
            }
        }

        /// <summary>
        /// The caller must update its reference to point to the new root
        /// </summary>
        public void SaveChanges()
        {
            for (ulong blockID : m_blocksToWrite)
            {
                Block block = m_blockBuffer[blockID];
                long offset = AllocationHelper.AllocateSpaceForBlock(m_file, block.TotalLength);
                block.WriteToStream(m_file.BaseStream, offset);
                m_file.BlockBTree.InsertBlockEntry(block.BlockID, offset, block.DataLength);
            }

            for (long blockID : m_blocksToFree)
            {
                BlockBTreeEntry entry = m_file.FindBlockEntryByBlockID(blockID);
                entry.cRef--;
                // Any leaf BBT entry that points to a BID holds a reference count to it.
                if (entry.cRef == 1)
                {
                    // we can mark the allocation to be freed and delete the entry,
                    // We should not free the allocation until the BBT is committed.
                    m_file.MarkAllocationToBeFreed(entry.BREF.ib, Block.GetTotalBlockLength(entry.cb));
                    m_file.BlockBTree.DeleteBlockEntry(entry.BREF.bid);
                }
                else
                {
                    m_file.BlockBTree.UpdateBlockEntry(entry.BREF.bid, entry.cRef);
                }
            }
            
            m_blocksToWrite.Clear();
            m_blocksToFree.Clear();
        }

        public PSTFile getFile()
            {
                return m_file;
            }
        }
}
