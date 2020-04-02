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
import com.hindsite.pst.types.PageType;
import com.hindsite.pst.utils.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import lombok.Data;

/**
 * 2.2.2.7.5
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
public final class FMapPage implements IPSTFileReader, IPSTFileWriter {

    public static final int size = 496 + PageTrailer.size;
    private byte[] rgbAMapBits; // 496 bytes
    private PageTrailer pageTrailer;

    public FMapPage() {
        rgbAMapBits = new byte[496];
        Arrays.fill(rgbAMapBits, (byte) 0);
        pageTrailer = new PageTrailer();
        pageTrailer.setPType(PageType.FreeMap);
        pageTrailer.setPTypeRepeat(PageType.FreeMap);
    }
    
    public FMapPage(InputStream in) throws IOException {
        read(in);
    }

    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        rgbAMapBits = StreamUtils.read(in, 496);
        pageTrailer.read(in);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        StreamUtils.write(out, rgbAMapBits);
        pageTrailer.write(out);
        return this;
    }

}
