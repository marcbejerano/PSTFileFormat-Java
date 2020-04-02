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

package com.hindsite.pst.pages;

import com.hindsite.pst.IPSTFileReader;
import com.hindsite.pst.IPSTFileWriter;
import com.hindsite.pst.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public final class DListPageEntry implements IPSTFileReader, IPSTFileWriter {

    public static final int size = 4; // 32 bits
    private int dwPageNum;
    private int dwFreeSlots;
    
    public DListPageEntry(InputStream in) throws IOException {
        read(in);
    }
    
    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        final int value = StreamUtils.readInt(in);
        dwPageNum = value >> 12;
        dwFreeSlots = value & 0x7F;
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        final int value = dwFreeSlots & 0x7F | (dwPageNum << 12);
        StreamUtils.write(out, value);
        return this;
    }
}
