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

import com.hindsite.pst.ndb.NodeID.Type;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author marcb
 */
public class NodeIDTest {
    
    public NodeIDTest() {
    }

    @BeforeEach
    public void setUp() {
    }
    
    /**
     * Test of write method, of class NodeID.
     * @throws java.lang.Exception
     */
    @Test
    public void testWrite() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NodeID instance = new NodeID(Type.NID_TYPE_NORMAL_FOLDER, 1234);
        instance.write(out);

        byte[] expected = new byte[]{ (byte) 0x10, (byte) 0x00, (byte) 0x04, (byte) 0xd2 };
        byte[] actual = out.toByteArray();
        
        assertArrayEquals(expected, actual);
    }

    /**
     * Test of read method, of class NodeID.
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        byte[] expected = new byte[]{ (byte) 0x10, (byte) 0x00, (byte) 0x04, (byte) 0xd2 };
        ByteArrayInputStream in = new ByteArrayInputStream(expected);
        NodeID instance = new NodeID(in);

        assertEquals(Type.NID_TYPE_NORMAL_FOLDER, instance.getNidType());        
        assertEquals(1234, instance.getNidIndex());
    }
    
}
