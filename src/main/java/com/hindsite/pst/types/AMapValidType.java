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
public enum AMapValidType {

    INVALID_AMAP(0x00), // One or more AMaps in the PST are INVALID
    VALID_AMAP1(0x01), // Deprecated. Implementations SHOULD NOT use this value. The AMaps are VALID
    VALID_AMAP2(0x02); // The AMaps are VALID. 

    private final int value;

    private AMapValidType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static AMapValidType valueOf(int value) {
        for (AMapValidType t : AMapValidType.values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        throw new EnumConstantNotPresentException(AMapValidType.class, "Unknown value: " + Integer.toHexString(value));
    }

}
