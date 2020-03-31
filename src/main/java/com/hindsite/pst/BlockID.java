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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

/**
 * 2.2.2.2
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Log
public final class BlockID implements IPSTFileReader, IPSTFileWriter {

    public static final int size = 8;
    
    private boolean internal;
    private long bidIndex;

    public BlockID(InputStream in) throws IOException {
        read(in);
    }
    
    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        long value = StreamUtils.readLong(in);
        this.internal = (value & 0x4000000000000000L) != 0;
        this.bidIndex = value &  0x3FFFFFFFFFFFFFFFL;
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        long value = this.bidIndex & 0x3FFFFFFFFFFFFFFFL;
        if (this.internal) {
            value |= 0x4000000000000000L;
        }
        StreamUtils.write(out, value);
        return this;
    }
}
