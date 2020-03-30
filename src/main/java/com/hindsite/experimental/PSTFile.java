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
package com.hindsite.experimental;

import com.hindsite.experimental.pstfile.nodedatabase.PSTHeader;
import com.hindsite.experimental.pstfile.nodedatabase.block.Block;
import com.hindsite.experimental.pstfile.nodedatabase.btree.BlockBTree;
import com.hindsite.experimental.pstfile.nodedatabase.btree.BlockBTreeEntry;
import java.io.OutputStream;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class PSTFile {

    private PSTHeader m_header;
    private BlockBTree m_blockBTree;

    public Block FindBlockByBlockID(long blockID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PSTHeader getHeader() {
        return m_header;
    }

    public OutputStream getBaseStream() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public BlocBTree getBlockBTree() {
        return m_blockBTree;
    }

    public BlockBTreeEntry FindBlockEntryByBlockID(long blockID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void MarkAllocationToBeFreed(long ib, int GetTotalBlockLength) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
