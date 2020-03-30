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
package com.hindsite.experimental.pstfile.nodedatabase.btree;

import com.hindsite.experimental.pstfile.nodedatabase.PSTCRCCalculation;
import com.hindsite.experimental.pstfile.nodedatabase.block.BlockRef;
import com.hindsite.experimental.pstfile.nodedatabase.block.BlockTrailer;
import com.hindsite.experimental.pstfile.nodedatabase.enums.PageTypeName;
import static com.hindsite.experimental.pstfile.nodedatabase.enums.PageTypeName.ptypeBBT;
import static com.hindsite.experimental.pstfile.nodedatabase.enums.PageTypeName.ptypeNBT;
import com.hindsite.experimental.pstfile.nodedatabase.exceptions.InvalidBlockIDException;
import com.hindsite.experimental.pstfile.nodedatabase.exceptions.InvalidChecksumException;
import com.hindsite.experimental.pstfile.nodedatabase.pages.Page;
import com.hindsite.experimental.pstfile.nodedatabase.pages.PageTrailer;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public abstract class BTreePage extends Page {
    // private byte cEnt;    // The number of BTree entries stored in the page data.

    public byte cEntMax;
    public byte cbEnt;   // The size of each BTree entry, in bytes. Implementations MUST use the size specified in cbEnt to advance to the next entry
    public byte cLevel;  // The depth level of this page. Leaf pages have a level of zero, whereas intermediate pages have a level greater than 0
    // public uint dwPadding

    public BTreeIndexPage ParentPage; // We use this to keep reference to the parent page
    public long Offset; // offset in PST file

    public BTreePage() {
        super();
    }

    public BTreePage(byte[] buffer) {
        super(buffer);

        byte cEnt = buffer[488];
        cEntMax = buffer[489];
        cbEnt = buffer[490];
        cLevel = buffer[491];

        PopulateEntries(buffer, cEnt);
    }

    public abstract void PopulateEntries(byte[] buffer, byte numberOfEntries);

    /// <returns>Number of entries</returns>
    public abstract int WriteEntries(byte[] buffer);

    @Override
    public byte[] GetBytes(long fileOffset) {
        byte[] buffer = new byte[Length];
        byte cEnt;

        cEnt = (byte) WriteEntries(buffer);
        buffer[488] = cEnt;
        buffer[489] = cEntMax;
        buffer[490] = cbEnt;
        buffer[491] = cLevel;
        pageTrailer.WriteToPage(buffer, fileOffset);

        return buffer;
    }

    public abstract long getPageKey();

    public static BTreePage ReadFromStream(InputStream stream, BlockRef blockRef) throws InvalidBlockIDException, InvalidChecksumException, InvalidChecksumException, IOException {
        long offset = (long) blockRef.ib;
        //stream.Seek(offset, SeekOrigin.Begin);
        byte[] buffer = new byte[Length];
        stream.read(buffer, 0, Length);
        PageTypeName ptype = PageTypeName.valueOf(buffer[PageTrailer.OffsetFromPageStart + 0]);
        BTreePage page;
        byte cLevel = buffer[491];
        if (cLevel > 0) {
            // If cLevel is greater than 0, then each entry in the array is of type BTENTRY.
            page = new BTreeIndexPage(buffer);
        } else {
            switch (ptype) {
                case ptypeBBT:
                    page = new BlockBTreeLeafPage(buffer);
                    break;
                case ptypeNBT:
                    page = new NodeBTreeLeafPage(buffer);
                    break;
                default:
                    throw new IllegalArgumentException("BTreePage has incorrect ptype");
            }
        }
        page.Offset = (long) blockRef.ib;

        if (blockRef.bid.getValue()
                != page.getBlockID().getValue()) {
            throw new InvalidBlockIDException();
        }
        int crc = PSTCRCCalculation.ComputeCRC(buffer, PageTrailer.OffsetFromPageStart);
        if (page.pageTrailer.dwCRC != crc) {
            throw new InvalidChecksumException();
        }

        int signature = BlockTrailer.ComputeSignature(blockRef.ib, blockRef.bid.getValue());
        if (page.pageTrailer.wSig != signature) {
            throw new InvalidChecksumException();
        }
        return page;
    }
}
