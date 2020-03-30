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
package com.hindsite.experimental.pstfile.nodedatabase;

import com.hindsite.experimental.pstfile.nodedatabase.enums.NodeTypeName;
import com.hindsite.experimental.utilities.byteutils.LittleEndianConverter;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class NodeID {

    public static final int MaximumNidIndex = 0x7FFFFFF;
    // nidType & nidIndex together comprise the unique NodeID

    // Note:
    // The documentation is not clear about the order of nidType and nidIndex,
    // it's bit reversed in [MS-PST]
    private int m_nodeID;

    public NodeID(byte[] buffer, int offset) {
        m_nodeID = LittleEndianConverter.getUInt32(buffer, offset + 0);
    }

    public NodeID(int nid) {
        m_nodeID = nid;
    }
    
    public NodeID(NodeID copy) {
        m_nodeID = copy.m_nodeID;
    }

    public NodeID(NodeTypeName nidType, int nidIndex) {
        m_nodeID = (byte) ((byte) nidType.getValue() & 0x1F);
        m_nodeID |= (nidIndex << 5);
    }

    public NodeTypeName getNidType() {
        return NodeTypeName.valueOf((byte) (m_nodeID & 0x1F));
    }

    public int getNidIndex() {
        return (m_nodeID >> 5);
    }

    public int getValue() {
        return m_nodeID;
    }
}
