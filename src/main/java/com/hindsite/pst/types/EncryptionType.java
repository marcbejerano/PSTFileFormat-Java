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

    NDB_CRYPT_NONE(0x00), // Data blocks are not encoded.
    NDB_CRYPT_PERMUTE(0x01), // Encoded with the Permutation algorithm (section 5.1).
    NDB_CRYPT_CYCLIC(0x02), // Encoded with the Cyclic algorithm (section 5.2).
    NDB_CRYPT_EDPCRYPTED(0x10); // Encrypted with Windows Information Protection.

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
