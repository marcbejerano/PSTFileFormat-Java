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
public class SubnodeIntermediateEntry {

    public static final int Length = 16;

    public NodeID nid;  // The key NID value to the next-level child block
    public BlockID bid; // The BID of the SLBLOCK

    public SubnodeIntermediateEntry() {
    }

    public SubnodeIntermediateEntry(NodeID nodeID, BlockID blockID) {
        nid = nodeID;
        bid = blockID;
    }

    public SubnodeIntermediateEntry(SubnodeIntermediateEntry copy) {
        nid = new NodeID(copy.nid);
        bid = new BlockID(copy.bid);
    }

    public SubnodeIntermediateEntry(byte[] buffer, int offset) {
        // the 4-byte NID is extended to its 8-byte equivalent
        nid = new NodeID(buffer, offset);
        bid = new BlockID(buffer, offset + 8);
    }

    public void WriteBytes(byte[] buffer, int offset) {
        // the 4-byte NID is extended to its 8-byte equivalent
        LittleEndianWriter.putUInt32(buffer, offset + 0, nid.getValue());
        LittleEndianWriter.putUInt64(buffer, offset + 8, bid.getValue());
    }

    public SubnodeIntermediateEntry Clone() {
        return new SubnodeIntermediateEntry(nid.Clone(), bid.Clone());
    }
}
