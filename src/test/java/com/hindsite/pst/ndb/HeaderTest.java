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

import java.io.File;
import java.io.FileInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author marcb
 */
public class HeaderTest {
    
    /**
     * Test of read method, of class Header.
     */
    @Test
    public void testRead() throws Exception {
        File file = new File("/home/marcb/Projects/backup1.pst");
        FileInputStream in = new FileInputStream(file);
        Header header = new Header(in);

        assertEquals(0x4e444221, header.getDwMagic());
        assertEquals(0x4d53, header.getWMagicClient());
        assertEquals(23, header.getWVer());
        assertEquals(19, header.getWVerClient());
        
        System.out.println(header.toString());
    }

    /**
     * Test of write method, of class Header.
     */
    @Test
    public void testWrite() throws Exception {
    }
    
}
