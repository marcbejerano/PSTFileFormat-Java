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

import com.hindsite.pst.types.EncryptionType;
import com.hindsite.pst.utils.EncryptionUtils;
import com.hindsite.pst.IPSTFileReader;
import com.hindsite.pst.IPSTFileWriter;
import com.hindsite.pst.utils.StreamUtils;
import com.hindsite.pst.ndb.BlockID;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.CRC32;
import lombok.Data;
import lombok.extern.java.Log;

/**
 * 2.2.2.6
 *
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
@Log
public final class Header implements IPSTFileReader, IPSTFileWriter {

    public static final int size = 564;
    private byte[] rawData;

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
    private NodeID[] rgnid; // 32 NodeID's
    private Root root;
    private int dwAlign;
    private byte[] rgbfm; // 128 bytes - no longer used, must be FF
    private byte[] rgbfp; // 128 bytes - no longer used, must be FF
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
        try {
            assert (dwMagic == 0x4e444221);
            assert (wMagicClient == 0x4d53);
            assert (wVer >= 23); // Only Unicode version is supported
            assert (wVerClient == 19);
            // dwCRCPartial (4 bytes): The 32-bit cyclic redundancy check (CRC) value of the 471 bytes of
            // data starting from wMagicClient (0ffset 0x0008)
            CRC32 crc32 = new CRC32();
            crc32.update(Arrays.copyOfRange(rawData, 8, 8 + 471));
            int crc = EncryptionUtils.computeCRC(0, Arrays.copyOfRange(rawData, 8, 8 + 471));
            log.info(String.format("crc=0x%04x, crc32=0x%04x, dwCRCPartial=0x%04x", crc, crc32.getValue(), dwCRCPartial));
            //assert (dwCRCPartial == crc);
            assert (bPlatformCreate == (byte) 0x01);
            assert (bPlatformAccess == (byte) 0x01);
            // dwCRCFull (4 bytes): The 32-bit CRC value of the 516 bytes of data starting from wMagicClient to bidNextB, inclusive.
            crc32.reset();
            crc32.update(Arrays.copyOfRange(rawData, 8, 8 + 516));
            crc = EncryptionUtils.computeCRC(0, Arrays.copyOfRange(rawData, 8, 8 + 516));
            log.info(String.format("crc=0x%04x, crc32=0x%04x, dwCRCFull=0x%04x", crc, crc32.getValue(), dwCRCFull));
        } catch (AssertionError e) {
            throw new RuntimeException("Invalid header");
        }
    }

    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        rawData = StreamUtils.read(in, size);
        ByteArrayInputStream bin = new ByteArrayInputStream(rawData);

        dwMagic = StreamUtils.readInt(bin);
        dwCRCPartial = StreamUtils.readInt(bin);
        wMagicClient = StreamUtils.readShort(bin);
        wVer = StreamUtils.readShort(bin);
        wVerClient = StreamUtils.readShort(bin);
        bPlatformCreate = StreamUtils.readByte(bin);
        bPlatformAccess = StreamUtils.readByte(bin);
        StreamUtils.readInt(bin);
        StreamUtils.readInt(bin);
        bidUnused = new BlockID(bin);
        bidNextIP = new BlockID(bin);
        dwUnique = StreamUtils.readInt(bin);
        
        // There needs to be a generic way to write this...
        rgnid = new NodeID[32];
        for (int n = 0; n < 32; n++) { rgnid[n] = new NodeID(bin); }
        
        StreamUtils.readLong(bin);
        root = new Root(bin);
        dwAlign = StreamUtils.readInt(bin);
        rgbfm = StreamUtils.read(bin, 128);
        rgbfp = StreamUtils.read(bin, 128);
        bSentinel = StreamUtils.readByte(bin);
        bCryptMethod = EncryptionType.valueOf(StreamUtils.readByte(bin));
        StreamUtils.readShort(bin); // rgbReserved
        bidNextB = new BlockID(bin);
        dwCRCFull = StreamUtils.readInt(bin);
        StreamUtils.readInt(bin); // rgbReserved2, bReserved
        StreamUtils.read(bin, 32); // rgbReserved3
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(size);

        StreamUtils.write(bout, dwMagic);
        StreamUtils.write(bout, dwCRCPartial);
        StreamUtils.write(bout, wMagicClient);
        StreamUtils.write(bout, wVer);
        StreamUtils.write(bout, wVerClient << 16 | (((int) bPlatformCreate & 0xFF) << 8) | ((int) bPlatformAccess & 0xFF));
        StreamUtils.write(bout, (int) 0); // dwReserved1
        StreamUtils.write(bout, (int) 0); // dwReserved2
        bidUnused.write(bout);
        bidNextIP.write(bout);
        StreamUtils.write(bout, dwUnique);
        StreamUtils.write(bout, rgnid);
        StreamUtils.write(bout, (long) 0L);
        root.write(bout);
        StreamUtils.write(bout, (int) 0);
        StreamUtils.write(bout, rgbfm);
        StreamUtils.write(bout, rgbfp);
        StreamUtils.write(bout, ((int) bSentinel & 0xFF) << 24 | ((int) bCryptMethod.getValue() & 0xFF << 16));
        bidNextB.write(bout);
        StreamUtils.write(bout, dwCRCFull);
        StreamUtils.write(bout, (int) 0); // rgbReserved2, bReserved
        StreamUtils.write(bout, new byte[32]); // rgbReserved3

        StreamUtils.write(out, bout.toByteArray());
        return this;
    }

}
