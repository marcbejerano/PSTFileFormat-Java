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
package com.hindsite.pst.types;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public enum PageType {

    BlockBTree((byte) 0x80),        // ptypeBBT - Block BTree page. Block or page signature (section 5.5).
    NodeBTree((byte) 0x81),         // ptypeNBT - Node BTree page. Block or page signature (section 5.5).
    FreeMap((byte) 0x82),           // ptypeFMap - Free Map page. 0x0000
    AllocationPageMap((byte) 0x83), // ptypePMap - Allocation Page Map page. 0x0000
    AllocationMap((byte) 0x84),     // ptypeAMap - Allocation Map page. 0x0000
    FreePageMap((byte) 0x85),       // ptypeFPMap - Free Page Map page. 0x0000
    DensityList((byte) 0x86);       // ptypeDL - Density List page. Block or page signature (section 5.5).

    private final byte value;

    private PageType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }

    public static PageType valueOf(int value) {
        for (PageType t : PageType.values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        throw new EnumConstantNotPresentException(PageType.class, "Unknown value: " + Integer.toHexString(value));
    }

}
