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
public class DListPage implements IPSTFileReader, IPSTFileWriter {

    private byte bFlags;
    private byte bEntDList;
    private int ulCurrentPage;
    private DListPageEntry[] rgDListPageEnt; // 476 bytes
    private PageTrailer pageTrailer;
    
    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        final int value = StreamUtils.readInt(in);
        bFlags = (byte) (value >> 24);
        bEntDList = (byte) (value >> 16 & 0xFF);
        ulCurrentPage = StreamUtils.readInt(in);

        
//        rgDListPageEnt = new DListPageEntry[119];
//        for (int n = 0; n < 119; n++) rgDListPageEnt[n] = new DListPageEntry(in);
//        StreamUtils.read(in, 12);

        pageTrailer = new PageTrailer(in);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        final int value = ((int) bFlags) << 24 | ((int) bEntDList) << 16;
        StreamUtils.write(out, value);
        StreamUtils.write(out, ulCurrentPage);
        for (int n = 0; n < 119; n++) rgDListPageEnt[n].write(out);
        final byte[] padding = new byte[12];
        StreamUtils.write(out, padding);
        pageTrailer.write(out);
        return this;
    }

}
