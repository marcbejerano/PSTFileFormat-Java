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
public enum BCryptMethodName {

    NDB_CRYPT_NONE((byte)0x00), // No Encryption
    NDB_CRYPT_PERMUTE((byte)0x01), // Compressible Encryption
    NDB_CRYPT_CYCLIC((byte)0x02);  // HighEncryption

    private final byte value;

    private BCryptMethodName(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
