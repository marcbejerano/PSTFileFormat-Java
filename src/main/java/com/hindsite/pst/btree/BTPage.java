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

package com.hindsite.pst.btree;

import com.hindsite.pst.IPSTFileReader;
import com.hindsite.pst.IPSTFileWriter;
import com.hindsite.pst.pages.PageTrailer;
import com.hindsite.pst.utils.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
public final class BTPage implements IPSTFileReader, IPSTFileWriter {

    public static final int size = 512;
    
    private byte[] rgentries; // 488 bytes
    private byte cEnt;
    private byte cEntMax;
    private byte cbEnt;
    private byte cLevel;
    private PageTrailer pageTrailer;
    
    public BTPage(InputStream in) throws IOException {
        read(in);
    }

    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        rgentries = StreamUtils.read(in, 488);
        cEnt = StreamUtils.readByte(in);
        cEntMax = StreamUtils.readByte(in);
        cbEnt = StreamUtils.readByte(in);
        cLevel = StreamUtils.readByte(in);
        pageTrailer = new PageTrailer(in);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        StreamUtils.write(out, rgentries);
        StreamUtils.write(out, cEnt);
        StreamUtils.write(out, cEntMax);
        StreamUtils.write(out, cbEnt);
        StreamUtils.write(out, cLevel);
        pageTrailer.write(out);
        return this;
    }
}
