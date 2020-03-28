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
package com.hindsite.experimental.pstfile.nodedatabase.pages;

import com.hindsite.experimental.pstfile.nodedatabase.block.BlockID;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author marcb
 */
public abstract class Page {

    public static final int Length = 512;
    public PageTrailer pageTrailer;

    public Page() {
        pageTrailer = new PageTrailer();
    }

    public Page(byte[] buffer) {
        pageTrailer = PageTrailer.ReadFromPage(buffer);
    }

    public abstract byte[] GetBytes(long fileOffset);

    public BlockID getBlockID() {
        return pageTrailer.bid;
    }

    public void setBlockId(long value) {
        pageTrailer.bid.setValue(value);
    }

    public void WriteToStream(OutputStream stream, long offset) throws IOException {
        stream.write(GetBytes((long) offset), 0, Length);
    }
}
