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

import com.hindsite.pst.IPSTFileReader;
import com.hindsite.pst.IPSTFileWriter;
import com.hindsite.pst.StreamUtils;
import com.hindsite.pst.ndb.BlockID;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

/**
 * 2.2.2.4
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Log
public final class BlockRef implements IPSTFileReader, IPSTFileWriter {

    public static final int size = 8 + BlockID.size;
    
    private BlockID bid;
    private long ib;

    public BlockRef(InputStream in) throws IOException {
        read(in);
    }
    
    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        bid = new BlockID(in);
        ib = StreamUtils.readLong(in);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        bid.write(out);
        StreamUtils.write(out, ib);
        return this;
    }
}
