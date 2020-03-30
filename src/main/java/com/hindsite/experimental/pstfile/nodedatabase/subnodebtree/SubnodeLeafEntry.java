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
package com.hindsite.experimental.pstfile.nodedatabase.subnodebtree;

import com.hindsite.experimental.pstfile.nodedatabase.NodeID;
import com.hindsite.experimental.pstfile.nodedatabase.block.BlockID;
import com.hindsite.experimental.utilities.byteutils.LittleEndianWriter;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class SubnodeLeafEntry {

    public static final int Length = 24;

    public NodeID nid;
    public BlockID bidData;
    public BlockID bidSub;

    public SubnodeLeafEntry() {
    }

    public SubnodeLeafEntry(byte[] buffer, int offset) {
        // the 4-byte NID is extended to its 8-byte equivalent
        nid = new NodeID(buffer, offset + 0);
        bidData = new BlockID(buffer, offset + 8);
        bidSub = new BlockID(buffer, offset + 16);
    }

    public SubnodeLeafEntry(SubnodeLeafEntry copy) {
        nid = new NodeID(copy.nid);
        bidData = new BlockID(copy.bidData);
        bidSub = new BlockID(copy.bidSub);
    }

    public void WriteBytes(byte[] buffer, int offset) {
        // the 4-byte NID is extended to its 8-byte equivalent
        LittleEndianWriter.putUInt32(buffer, offset + 0, nid.getValue());
        LittleEndianWriter.putUInt64(buffer, offset + 8, bidData.getValue());
        LittleEndianWriter.putUInt64(buffer, offset + 16, bidSub.getValue());
    }

    public SubnodeLeafEntry Clone() {
        SubnodeLeafEntry result = new SubnodeLeafEntry();
        result.nid = nid.Clone();
        result.bidData = bidData.Clone();
        result.bidSub = bidSub.Clone();
        return result;
    }
}
