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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author marcb
 */
public class EncryptionUtilsTest {
    
    public EncryptionUtilsTest() {
    }

    /**
     * Test of permute method, of class EncryptionUtils.
     */
    @Test
    public void testPermute() {
        final String plaintext = "Hello world!";
        final byte[] encrypted = EncryptionUtils.permute(plaintext.getBytes(), true);
        final byte[] decrypted = EncryptionUtils.permute(encrypted, false);
        final String actual = new String(decrypted);
        assertEquals(plaintext, actual);
    }
    
}
