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
public enum EncryptionType {

    None(0x00),         // NDB_CRYPT_NONE - Data blocks are not encoded.
    Permute(0x01),      // NDB_CRYPT_PERMUTE - Encoded with the Permutation algorithm (section 5.1).
    Cyclic(0x02),       // NDB_CRYPT_CYCLIC - Encoded with the Cyclic algorithm (section 5.2).
    EDPCrypted(0x10);   // NDB_CRYPT_EDPCRYPTED - Encrypted with Windows Information Protection.

    private final int value;

    private EncryptionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static EncryptionType valueOf(int value) {
        for (EncryptionType t : EncryptionType.values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        throw new EnumConstantNotPresentException(EncryptionType.class, "Unknown value: " + Integer.toHexString(value));
    }

}
