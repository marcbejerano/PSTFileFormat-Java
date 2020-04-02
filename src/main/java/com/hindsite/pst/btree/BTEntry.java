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
import com.hindsite.pst.ndb.BlockRef;
import com.hindsite.pst.utils.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
@NoArgsConstructor
public final class BTEntry implements IPSTFileReader, IPSTFileWriter {

    private long btkey;
    private BlockRef bref;

    public BTEntry(InputStream in) throws IOException {
        read(in);
    }

    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        btkey = StreamUtils.readLong(in);
        bref = new BlockRef(in);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        StreamUtils.write(out, btkey);
        bref.write(out);
        return this;
    }
}
