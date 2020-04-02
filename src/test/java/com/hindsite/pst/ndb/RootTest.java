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

import com.hindsite.pst.types.AMapValidType;
import com.hindsite.pst.ndb.Root;
import com.hindsite.pst.ndb.BlockRef;
import com.hindsite.pst.ndb.BlockID;
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
public class RootTest {
    
    public RootTest() {
    }

    /**
     * Test of read method, of class Root.
     */
    @Test
    public void testRead() throws Exception {
        byte[] expected = new byte[]{
            // dwReserved
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            // ibFileEOF
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xbc, (byte) 0x61, (byte) 0x4e,
            // ibAMapLast
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x39, (byte) 0x7f, (byte) 0xb1,
            // cbAMapFree
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xe8,
            // cbPMapFree
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xd0,
            // BREFNBT
            (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xd2,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x85,
            // BREFBBT
            (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xd2,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x85,
            // fAMapValid
            (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        };
        ByteArrayInputStream in = new ByteArrayInputStream(expected);
        Root instance = new Root(in);
        
        assertEquals(12345678, instance.getIbFileEOF());
        assertEquals(87654321, instance.getIbAMapLast());
        assertEquals(1000, instance.getCbAMapFree());
        assertEquals(2000, instance.getCbPMapFree());
        assertTrue(instance.getBREFNBT().getBid().isInternal());
        assertEquals(1234, instance.getBREFNBT().getBid().getBidIndex());
        assertEquals(6789, instance.getBREFNBT().getIb());
        assertTrue(instance.getBREFBBT().getBid().isInternal());
        assertEquals(1234, instance.getBREFBBT().getBid().getBidIndex());
        assertEquals(6789, instance.getBREFBBT().getIb());
        assertEquals(AMapValidType.VALID_AMAP2, instance.getFAMapValid());
    }

    /**
     * Test of write method, of class Root.
     */
    @Test
    public void testWrite() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Root instance = new Root();
        instance.setIbFileEOF(12345678);
        instance.setIbAMapLast(87654321);
        instance.setCbAMapFree(1000);
        instance.setCbPMapFree(2000);
        instance.setBREFNBT(new BlockRef(new BlockID(true, 1234), 6789));
        instance.setBREFBBT(new BlockRef(new BlockID(true, 1234), 6789));
        instance.setFAMapValid(AMapValidType.VALID_AMAP2);
        instance.write(out);

        byte[] expected = new byte[]{
            // dwReserved
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            // ibFileEOF
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xbc, (byte) 0x61, (byte) 0x4e,
            // ibAMapLast
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x39, (byte) 0x7f, (byte) 0xb1,
            // cbAMapFree
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0xe8,
            // cbPMapFree
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xd0,
            // BREFNBT
            (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xd2,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x85,
            // BREFBBT
            (byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0xd2,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x85,
            // fAMapValid
            (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00,
        };
        byte[] actual = out.toByteArray();
        
        assertArrayEquals(expected, actual);
    }
}
