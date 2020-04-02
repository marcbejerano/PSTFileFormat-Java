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

    Invalid(0x00), // INVALID_AMAP - One or more AMaps in the PST are INVALID
    @Deprecated
    Valid_AllocationMap1(0x01), // VALID_AMAP1 - Deprecated. Implementations SHOULD NOT use this value. The AMaps are VALID
    Valid(0x02); // VALID_AMAP2 - The AMaps are VALID. 

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
