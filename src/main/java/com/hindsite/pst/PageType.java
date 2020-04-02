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
package com.hindsite.pst;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public enum PageType {

    BBT((byte) 0x80),   // Block BTree page. Block or page signature (section 5.5).
    NBT((byte) 0x81),   // Node BTree page. Block or page signature (section 5.5).
    FMap((byte) 0x82),  // Free Map page. 0x0000
    PMap((byte) 0x83),  // Allocation Page Map page. 0x0000
    AMAp((byte) 0x84),  // Allocation Map page. 0x0000
    FPMap((byte) 0x85), // Free Page Map page. 0x0000
    DL((byte) 0x86);    // Density List page. Block or page signature (section 5.5).

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
