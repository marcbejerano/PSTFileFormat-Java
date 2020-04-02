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
import com.hindsite.pst.ndb.BlockID;
import com.hindsite.pst.ndb.NodeID;
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
public final class NBTEntry implements IPSTFileReader, IPSTFileWriter {

    private NodeID nid;
    private BlockID bidData;
    private BlockID bidSub;
    private NodeID nidParent;

    public NBTEntry(InputStream in) throws IOException {
        read(in);
    }

    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        nid = new NodeID(in);
        StreamUtils.readInt(in);
        bidData = new BlockID(in);
        bidSub = new BlockID(in);
        nidParent = new NodeID(in);
        return this;
    }

    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        nid.write(out);
        StreamUtils.write(out, (int) 0);
        bidData.write(out);
        bidSub.write(out);
        nidParent.write(out);
        return this;
    }
}
