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
package com.hindsite.experimental.pstfile.nodedatabase;

import com.hindsite.experimental.pstfile.nodedatabase.block.BlockID;
import com.hindsite.experimental.pstfile.nodedatabase.enums.BCryptMethodName;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class PSTHeader {

    public static final int HeaderLength = 564;

    public enum PSTVersion {
        //Ansi = 14,
        //Ansi = 15,
        Unicode(23); // Unicode PST file

        private final int value;

        private PSTVersion(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ClientVersion {
        OfflineFolders(12), // OST file
        PersonalFolders(19); // PST file

        private final int value;

        private ClientVersion(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    // Note regarding dwReserved1, dwReserved2, rgbReserved2, bReserved, rgbReserved3:
        // Outlook 2003-2010 use these value for implementation-specific data.
        // Modification of these values can result in failure to read the PST file by Outlook

        public String dwMagic = "!BDN"; // !BDN
        public int CRCPartial;
        public String wMagicClient;
        public short wVer;
        public short wVerClient;
        public byte bPlatformCreate;
        public byte bPlatformAccess;
        public int dwReserved1; // offset 16, 
        public int dwReserved2;
        public BlockID bidUnused; // offset 24
        private BlockID bidNextP; // offset 32
        /* Note: no bidNextB here, documentation mistake, see http://social.msdn.microsoft.com/Forums/pl/os_binaryfile/thread/b50106b9-d1a0-4877-aaa5-8d23be1084fd */
        private int dwUnique; // offset 40
        private int[] rgnid = new int[32];
        // private ulong Unused - offset 172
        public RootStructure root; // offset 180
        public int dwAlign; // offset 252
        public byte[] rgbFM = new byte[128]; // offset 256
        public byte[] rgbFP = new byte[128]; // offset 384
        public byte bSentinel = (byte) 0x80; // offset 512, must be set to 0x80
        public BCryptMethodName bCryptMethod;
        public short rgbReserved;
        private BlockID bidNextB; // offset 516, Indicates the next available BID value
        public int dwCRCFull; // offset 524
        public byte[] rgbReserved2 = new byte[3];
        public byte bReserved;
        public byte[] rgbReserved3 = new byte[32];

}
