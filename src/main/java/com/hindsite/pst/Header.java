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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

/**
 * 2.2.2.6
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
public final class Header implements IPSTFileReader, IPSTFileWriter {

    private int dwMagic; // MUST be "{ 0x21, 0x42, 0x44, 0x4E } ("!BDN")".
    private int dwCRCPartial; // The 32-bit cyclic redundancy check (CRC) value of the 471 bytes of data starting from wMagicClient (0ffset 0x0008)
    private short wMagicClient; // MUST be "{ 0x53, 0x4D }"
    private short wVer;
    private short wVerClient;
    private byte bPlatformCreate;
    private byte bPlatformAccess;
    private BlockID bidUnused;
    private BlockID bidNextIP;
    private int dwUnique;
    private byte[] rgnid; // 128 bytes
    private Root root;
    private int dwAlign;
    private Root root2; //??
    private byte[] rgbfm; // 128 bytes
    private byte[] rgbfp; // 128 bytes
    private byte bSentinel;
    private EncryptionType bCryptMethod;
    private short rgbReserved;
    private BlockID bidNextB;
    private int dwCRCFull;
    private int rgbReserved2; // dwReserved2 + bReserved
    private byte[] rgbReserved3; // 32 bytes

    public Header(InputStream in) throws IOException {
        read(in);
        trustButVerify();
    }
    
    private void trustButVerify() {
        assert(dwMagic == 0x2142444e);
        assert(wMagicClient == 0x534d);
        assert(wVer > 23); // Only Unicode version is supported
        // CRC header ...
        assert(bPlatformCreate == (byte) 0x01);
        assert(bPlatformAccess == (byte) 0x01);
        // CRC full ...
    }
    
    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        dwMagic = StreamUtils.readInt(in);
        dwCRCPartial = StreamUtils.readInt(in);
        wMagicClient = StreamUtils.readShort(in);
        wVer = StreamUtils.readShort(in);
        wVerClient = StreamUtils.readShort(in);
        bPlatformCreate = StreamUtils.readByte(in);
        bPlatformAccess = StreamUtils.readByte(in);
        StreamUtils.readInt(in);
        StreamUtils.readInt(in);
        bidUnused = new BlockID(in);
        bidNextIP = new BlockID(in);
        dwUnique = StreamUtils.readInt(in);
        rgnid = StreamUtils.read(in, 128);
        StreamUtils.readLong(in);
        root = new Root(in);
        dwAlign = StreamUtils.readInt(in);
        rgbfm = StreamUtils.read(in, 128);
        rgbfp = StreamUtils.read(in, 128);
        bSentinel = StreamUtils.readByte(in);
        bCryptMethod = EncryptionType.valueOf(StreamUtils.readByte(in));
        StreamUtils.readShort(in); // rgbReserved
        bidNextB = new BlockID(in);
        dwCRCFull = StreamUtils.readInt(in);
        StreamUtils.readInt(in);
        StreamUtils.read(in, 32);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
