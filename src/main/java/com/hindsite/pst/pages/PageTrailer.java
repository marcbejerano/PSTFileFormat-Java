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
import com.hindsite.pst.PageType;
import com.hindsite.pst.StreamUtils;
import com.hindsite.pst.ndb.BlockID;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
public final class PageTrailer implements IPSTFileReader, IPSTFileWriter {

    public static final int size = 32;
    
    private PageType pType;
    private PageType pTypeRepeat;
    private short wSig;
    private BlockID bid;
    
    public PageTrailer(InputStream in) throws IOException {
        read(in);
    }

    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        pType = PageType.valueOf(StreamUtils.readByte(in));
        pTypeRepeat = PageType.valueOf(StreamUtils.readByte(in));
        wSig = StreamUtils.readShort(in);
        bid = new BlockID(in);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        StreamUtils.write(out, pType.getValue());
        StreamUtils.write(out, pTypeRepeat.getValue());
        StreamUtils.write(out, wSig);
        bid.write(out);
        return this;
    }
    
}
