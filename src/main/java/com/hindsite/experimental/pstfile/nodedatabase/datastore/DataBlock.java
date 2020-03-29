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
package com.hindsite.experimental.pstfile.nodedatabase.datastore;

import com.hindsite.experimental.pstfile.nodedatabase.PSTEncryptionUtils;
import com.hindsite.experimental.pstfile.nodedatabase.block.Block;
import com.hindsite.experimental.pstfile.nodedatabase.enums.BCryptMethodName;
import com.hindsite.experimental.utilities.byteutils.ByteWriter;
import java.util.Arrays;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public final class DataBlock extends Block {

    public static final int MaximumDataLength = 8176; // Block.MaximumLength - BlockTrailer.Length;

    private BCryptMethodName m_bCryptMethod;
    public byte[] Data = new byte[0];

    public DataBlock(BCryptMethodName bCryptMethod) {
        super();
        m_bCryptMethod = bCryptMethod;
    }

    public DataBlock(byte[] buffer, BCryptMethodName bCryptMethod) {
        super(buffer);
        m_bCryptMethod = bCryptMethod;
        Data = new byte[blockTrailer.cb];
        Data = Arrays.copyOf(buffer, blockTrailer.cb);

        // DataBlock's data may be decoded
        Data = GetDecodedData();
    }

    @Override
    public void WriteDataBytes(byte[] buffer, int offset) {
        byte[] data = GetEncodedData();
        ByteWriter.putBytes(buffer, offset, data);
        offset += data.length;
    }

    public byte[] GetDecodedData() {
        byte[] result = new byte[Data.length];
        Data = Arrays.copyOf(result, Data.length);
        if (m_bCryptMethod == BCryptMethodName.NDB_CRYPT_PERMUTE) {
            PSTEncryptionUtils.CryptPermute(result, result.length, false);
        } else if (m_bCryptMethod == BCryptMethodName.NDB_CRYPT_CYCLIC) {
            // The block trailer was supposed to be read at this stage.
            // [MS-PST]: the value to use for dwKey is the lower DWORD of the BID
            // associated with this data block.
            int key = (int) (getBlockID().getValue() & 0xFFFFFFFF);
            PSTEncryptionUtils.CryptCyclic(result, result.length, key);
        }
        return result;
    }

    public byte[] GetEncodedData() {
        byte[] result = new byte[Data.length];
        Data = Arrays.copyOf(result, Data.length);
        if (m_bCryptMethod == BCryptMethodName.NDB_CRYPT_PERMUTE) {
            PSTEncryptionUtils.CryptPermute(result, result.length, true);
        } else if (m_bCryptMethod == BCryptMethodName.NDB_CRYPT_CYCLIC) {
            // [MS-PST]: the value to use for dwKey is the lower DWORD of the BID
            // associated with this data block.
            int key = (int) (getBlockID().getValue() & 0xFFFFFFFF);
            PSTEncryptionUtils.CryptCyclic(result, result.length, key);
        }
        return result;
    }

    @Override
    public Block Clone() throws CloneNotSupportedException {
        DataBlock result = (DataBlock) this.clone();
        result.Data = new byte[Data.length];
        result.Data = Arrays.copyOf(Data, Data.length);
        return result;
    }

    // Raw data contained in the block (excluding trailer and alignment padding)
    @Override
    public int getDataLength() {
        return Data.length;
    }
}
