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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author marcb
 */
public class BlockRefTest {
    
    public BlockRefTest() {
    }

    /**
     * Test of read method, of class BlockRef.
     */
    @Test
    public void testRead() throws Exception {
        byte[] expected = new byte[]{
            (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xd2,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x85
        };
        ByteArrayInputStream in = new ByteArrayInputStream(expected);
        BlockRef instance = new BlockRef(in);
        
        assertTrue(instance.getBid().isInternal());
        assertEquals(1234, instance.getBid().getBidIndex());
        assertEquals(6789, instance.getIb());
    }

    /**
     * Test of write method, of class BlockRef.
     */
    @Test
    public void testWrite() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BlockRef instance = new BlockRef(new BlockID(true, 1234), 6789);
        instance.write(out);

        byte[] expected = new byte[]{
            (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xd2,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x85
        };
        byte[] actual = out.toByteArray();
        
        assertArrayEquals(expected, actual);
    }

}
