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

import com.hindsite.pst.AMapValidType;
import com.hindsite.pst.IPSTFileReader;
import com.hindsite.pst.IPSTFileWriter;
import com.hindsite.pst.StreamUtils;
import com.hindsite.pst.ndb.BlockRef;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

/**
 * 2.2.2.5
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
@NoArgsConstructor
@Log
public final class Root implements IPSTFileReader, IPSTFileWriter {

    public static final int size = 4 + (4 * 8) + (2 * BlockRef.size) + 4;
    
    private long ibFileEOF;
    private long ibAMapLast;
    private long cbAMapFree;
    private long cbPMapFree;
    private BlockRef BREFNBT; // 16 bytes
    private BlockRef BREFBBT; // 16 bytes
    private AMapValidType fAMapValid;

    public Root(InputStream in) throws IOException {
        read(in);
    }

    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        StreamUtils.readInt(in); // dwReserved
        ibFileEOF = StreamUtils.readLong(in);
        ibAMapLast = StreamUtils.readLong(in);
        cbAMapFree = StreamUtils.readLong(in);
        cbPMapFree = StreamUtils.readLong(in);
        BREFNBT = new BlockRef(in);
        BREFBBT = new BlockRef(in);
        int value = StreamUtils.readInt(in);
        fAMapValid = AMapValidType.valueOf(value >> 24);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        StreamUtils.write(out, (int) 0);
        StreamUtils.write(out, ibFileEOF);
        StreamUtils.write(out, ibAMapLast);
        StreamUtils.write(out, cbAMapFree);
        StreamUtils.write(out, cbPMapFree);
        BREFNBT.write(out);
        BREFBBT.write(out);
        int value = (fAMapValid.getValue() & 0x03) << 24;
        StreamUtils.write(out, value);
        return this;
    }
    
    
}
