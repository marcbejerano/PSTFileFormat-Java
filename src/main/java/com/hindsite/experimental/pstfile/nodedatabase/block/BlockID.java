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

import com.hindsite.experimental.utilities.byteutils.LittleEndianConverter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianWriter;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public final class BlockID {

    public static final long MaximumBidIndex = 0xFFFFFFFFFFFFFFFL;
    public static final int Length = 8;

    // Reserved & Internel & bidIndex together comprise the unique BlockID
    private long m_blockID;

    public BlockID(long blockID) {
        m_blockID = blockID;
    }

    public BlockID(boolean isInternal, long bidIndex) {
        setInternal(isInternal);
        setBidIndex(bidIndex);
    }
    
    public BlockID(BlockID copy) {
        this.m_blockID = copy.m_blockID;
    }

    public BlockID(byte[] buffer, int offset) {
        m_blockID = LittleEndianConverter.getUInt64(buffer, offset + 0);
    }

    public void WriteBytes(byte[] buffer, int offset) {
        LittleEndianWriter.putUInt64(buffer, offset + 0, m_blockID);
    }

    public long getValue() {
        return m_blockID;
    }
    
    public void setValue(long value) {
        this.m_blockID = value;
    }

    public long getLookupValue() {
        // Readers MUST ignore the reserved bit and treat it as zero before looking up the BID from the BBT
        return m_blockID & 0xFFFFFFFFFFFFFFFEL;
    }

    // first bit is 'reserved'
    // Office Outlook 2003, Office Outlook 2007, and Outlook 2010 use the reserved bit for implementation-specific data
    public boolean isReserved() {
        return (m_blockID & 0x01) != 0;
    }

    // second bit is 'internal'
    public boolean isInternal() {
        return (m_blockID & 0x02) != 0;
    }

    public void setInternal(boolean value) {
        if (value) {
            m_blockID |= 0x02;
        } else {
            m_blockID &= 0xFFFFFFFD;
        }

    }

    public long getBidIndex() {
        return m_blockID >> 2;
    }

    public void setBidIndex(long value) {
        m_blockID &= 0x03;
        m_blockID |= (value << 2);
    }

    @Override
    public BlockID clone() {
        return new BlockID(this);
    }
}
