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
package com.hindsite.pst.ndb;

import com.hindsite.pst.ndb.BlockID;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author marcb
 */
public class BlockIDTest {
    
    public BlockIDTest() {
    }

    /**
     * Test of read method, of class BlockID.
     */
    @Test
    public void testWrite() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BlockID instance = new BlockID(true, 1234);
        instance.write(out);

        byte[] expected = new byte[]{ (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xd2 };
        byte[] actual = out.toByteArray();
        
        assertArrayEquals(expected, actual);
    }

    /**
     * Test of write method, of class BlockID.
     */
    @Test
    public void testRead() throws Exception {
        byte[] expected = new byte[]{ (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xd2 };
        ByteArrayInputStream in = new ByteArrayInputStream(expected);
        BlockID instance = new BlockID(in);

        assertTrue(instance.isInternal());
        assertEquals(1234, instance.getBidIndex());
    }

}
