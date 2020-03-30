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

import com.hindsite.experimental.PSTFile;
import com.hindsite.experimental.pstfile.nodedatabase.enums.PageTypeName;

/**
 *
 * @author marcb
 */
public class AllocationMapPage extends Page {

    public static final int FirstPageOffset = 0x4400; // offset of the first AMAPPAGE within the PST file
    public static final int MapppedLength = 253952; // the number of bytes mapped by an AMap (496 * 8 * 64)

    public byte[] rgbAMapBits = new byte[496];

    public AllocationMapPage() {
        super();
        rgbAMapBits[0] = (byte) 0xFF; // An AMap is allocated out of the data section
        pageTrailer.ptype = PageTypeName.ptypeAMap;
        pageTrailer.wSig = 0x00; // zero for AMap
    }

    public AllocationMapPage(byte[] buffer) {
        super(buffer);
        System.arraycopy(buffer, 0, rgbAMapBits, 0, rgbAMapBits.length);
    }

    /// <param name="fileOffset">Irrelevant for AMap</param>
    @Override
    public byte[] GetBytes(long fileOffset) {
        byte[] buffer = new byte[Length];
        System.arraycopy(rgbAMapBits, 0, buffer, 0, rgbAMapBits.length);
        pageTrailer.WriteToPage(buffer, fileOffset);
        return buffer;
    }

    /// <returns>offset within the mapped space, or -1 if no such allocation possible</returns>
    public int FindContiguousSpace(int allocationLength, boolean pageAligned) {
        int firstFreeOffset = -1;
        int freeLength = 0;
        for (int byteIndex = 0; byteIndex < rgbAMapBits.length; byteIndex++) {
            for (int bitNumber = 0; bitNumber < 8; bitNumber++) {
                // from MSB to LSB (as suggested by MS-PST, page 68)
                // The MSB represents the first bit in the map
                int bitOffset = 7 - bitNumber;
                if (bitNumber > 0 && pageAligned) {
                    if (freeLength == 0) // means that we are not in a middle of a free space sequence
                    {
                        break; // we're looking for page aligned space, scan next page
                    }
                }

                boolean isFree = (((byte) (rgbAMapBits[byteIndex] >> bitOffset)) & 0x01) == 0;
                if (isFree) {
                    if (freeLength == 0) {
                        // first free bit
                        firstFreeOffset = byteIndex * 8 * 64 + bitNumber * 64;
                    }

                    freeLength += 64; // each bit represents 64 bytes
                    if (freeLength >= allocationLength) {
                        return firstFreeOffset;
                    }
                } else {
                    freeLength = 0;
                }
            }
        }

        return -1;
    }

    /// <summary>
    /// We assume allocated space is free
    /// </summary>
    /// <param name="startOffset">offset within the mapped space</param>
    /// <param name="allocationLength">length (in bytes) within the mapped space</param>
    public void AllocateSpace(int startOffset, int allocationLength) {
        for (int offset = startOffset; offset < startOffset + allocationLength; offset += 64) {
            AllocateUnit(offset);
        }
    }

    /// <summary>
    /// Unit is 64 bytes
    /// </summary>
    /// <param name="startOffset">Start offset of the unit to allocate</param>
    public void AllocateUnit(int startOffset) {
        int byteOffset = startOffset / (8 * 64);
        // from MSB to LSB (as suggested by MS-PST, page 68)
        int bitNumber = (startOffset % (8 * 64)) / 64;
        int bitOffset = 7 - bitNumber;
        rgbAMapBits[byteOffset] |= (byte) (0x01 << bitOffset);
    }

    public void FreeAllocatedSpace(int startOffset, int allocationLength) {
        for (int offset = startOffset; offset < startOffset + allocationLength; offset += 64) {
            FreeAllocatedUnit(offset);
        }
    }

    /// <summary>
    /// Unit is 64 bytes
    /// </summary>
    /// <param name="startOffset">Start offset of the unit to free</param>
    public void FreeAllocatedUnit(int startOffset) {
        int byteOffset = startOffset / (8 * 64);
        // from MSB to LSB (as suggested by MS-PST, page 68)
        int bitNumber = (startOffset % (8 * 64)) / 64;
        int bitOffset = 7 - bitNumber;
        rgbAMapBits[byteOffset] &= (byte) ~(0x01 << bitOffset);
    }

    /// <returns>Max contiguous space in bytes</returns>
    public int GetMaxContiguousSpace() {
        int freeLength = 0;
        int max = 0;

        for (int byteIndex = 0; byteIndex < rgbAMapBits.length; byteIndex++) {
            for (int bitNumber = 0; bitNumber < 8; bitNumber++) {
                // from MSB to LSB (as suggested by MS-PST, page 68)
                // The MSB represents the first bit in the map
                int bitOffset = 7 - bitNumber;

                boolean isFree = (((byte) (rgbAMapBits[byteIndex] >> bitOffset)) & 0x01) == 0;
                if (isFree) {
                    freeLength += 64; // each bit represents 64 bytes
                    if (freeLength > max) {
                        max = freeLength;
                    }
                } else {
                    freeLength = 0;
                }
            }
        }

        return max;
    }

    public void WriteAllocationMapPage(PSTFile file, int allocationMapPageIndex) {
        throw new RuntimeException("Not implemented");
//        long offset = FirstPageOffset + (long) MapppedLength * allocationMapPageIndex;
//        WriteToStream(file.BaseStream, offset);
    }

    public static AllocationMapPage ReadAllocationMapPage(PSTFile file, int allocationMapPageIndex) {
        throw new RuntimeException("Not implemented");
//        // The first AMap of a PST file is always located at absolute file offset 0x4400, and subsequent AMaps appear at intervals of 253,952 bytes thereafter
//        long offset = FirstPageOffset + (long) MapppedLength * allocationMapPageIndex;
//        //file.BaseStream.Seek(offset, SeekOrigin.Begin);
//        byte[] buffer = new byte[Length];
//        file.BaseStream.Read(buffer, 0, Length);
//
//        return new AllocationMapPage(buffer);
    }
}
