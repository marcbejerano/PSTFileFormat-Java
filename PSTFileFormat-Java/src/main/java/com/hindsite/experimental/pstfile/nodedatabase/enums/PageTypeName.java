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
package com.hindsite.experimental.pstfile.nodedatabase.enums;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public enum PageTypeName {
    ptypeBBT((byte)0x80), // Block BTree page.
    ptypeNBT((byte)0x81), // Node BTree page
    ptypeFMap((byte)0x82), // Free Map page
    ptypePMap((byte)0x83), // Allocation Page Map page
    ptypeAMap((byte)0x84), // Allocation Map page
    ptypeFPMap((byte)0x85), // Free Page Map page
    ptypeDL((byte)0x86);    // Density List page

    private final byte value;

    private PageTypeName(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
    
    public static PageTypeName valueOf(byte value) {
        switch (value) {
            case (byte)0x80: return ptypeBBT;
            case (byte)0x81: return ptypeNBT;
            case (byte)0x82: return ptypeFMap;
            case (byte)0x83: return ptypePMap;
            case (byte)0x84: return ptypeAMap;
            case (byte)0x85: return ptypeFPMap;
            case (byte)0x86: return ptypeDL;
            default:
                throw new IllegalArgumentException(String.format("Unsupported value 0x%02x", value));
        }
    }
}
