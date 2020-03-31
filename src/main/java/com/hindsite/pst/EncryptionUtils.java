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
package com.hindsite.pst;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class EncryptionUtils {

    private static final byte mpbbCrypt[] = {
        (byte) 65, (byte) 54, (byte) 19, (byte) 98, (byte) 168, (byte) 33, (byte) 110, (byte) 187,
        (byte) 244, (byte) 22, (byte) 204, (byte) 4, (byte) 127, (byte) 100, (byte) 232, (byte) 93,
        (byte) 30, (byte) 242, (byte) 203, (byte) 42, (byte) 116, (byte) 197, (byte) 94, (byte) 53,
        (byte) 210, (byte) 149, (byte) 71, (byte) 158, (byte) 150, (byte) 45, (byte) 154, (byte) 136,
        (byte) 76, (byte) 125, (byte) 132, (byte) 63, (byte) 219, (byte) 172, (byte) 49, (byte) 182,
        (byte) 72, (byte) 95, (byte) 246, (byte) 196, (byte) 216, (byte) 57, (byte) 139, (byte) 231,
        (byte) 35, (byte) 59, (byte) 56, (byte) 142, (byte) 200, (byte) 193, (byte) 223, (byte) 37,
        (byte) 177, (byte) 32, (byte) 165, (byte) 70, (byte) 96, (byte) 78, (byte) 156, (byte) 251,
        (byte) 170, (byte) 211, (byte) 86, (byte) 81, (byte) 69, (byte) 124, (byte) 85, (byte) 0,
        (byte) 7, (byte) 201, (byte) 43, (byte) 157, (byte) 133, (byte) 155, (byte) 9, (byte) 160,
        (byte) 143, (byte) 173, (byte) 179, (byte) 15, (byte) 99, (byte) 171, (byte) 137, (byte) 75,
        (byte) 215, (byte) 167, (byte) 21, (byte) 90, (byte) 113, (byte) 102, (byte) 66, (byte) 191,
        (byte) 38, (byte) 74, (byte) 107, (byte) 152, (byte) 250, (byte) 234, (byte) 119, (byte) 83,
        (byte) 178, (byte) 112, (byte) 5, (byte) 44, (byte) 253, (byte) 89, (byte) 58, (byte) 134,
        (byte) 126, (byte) 206, (byte) 6, (byte) 235, (byte) 130, (byte) 120, (byte) 87, (byte) 199,
        (byte) 141, (byte) 67, (byte) 175, (byte) 180, (byte) 28, (byte) 212, (byte) 91, (byte) 205,
        (byte) 226, (byte) 233, (byte) 39, (byte) 79, (byte) 195, (byte) 8, (byte) 114, (byte) 128,
        (byte) 207, (byte) 176, (byte) 239, (byte) 245, (byte) 40, (byte) 109, (byte) 190, (byte) 48,
        (byte) 77, (byte) 52, (byte) 146, (byte) 213, (byte) 14, (byte) 60, (byte) 34, (byte) 50,
        (byte) 229, (byte) 228, (byte) 249, (byte) 159, (byte) 194, (byte) 209, (byte) 10, (byte) 129,
        (byte) 18, (byte) 225, (byte) 238, (byte) 145, (byte) 131, (byte) 118, (byte) 227, (byte) 151,
        (byte) 230, (byte) 97, (byte) 138, (byte) 23, (byte) 121, (byte) 164, (byte) 183, (byte) 220,
        (byte) 144, (byte) 122, (byte) 92, (byte) 140, (byte) 2, (byte) 166, (byte) 202, (byte) 105,
        (byte) 222, (byte) 80, (byte) 26, (byte) 17, (byte) 147, (byte) 185, (byte) 82, (byte) 135,
        (byte) 88, (byte) 252, (byte) 237, (byte) 29, (byte) 55, (byte) 73, (byte) 27, (byte) 106,
        (byte) 224, (byte) 41, (byte) 51, (byte) 153, (byte) 189, (byte) 108, (byte) 217, (byte) 148,
        (byte) 243, (byte) 64, (byte) 84, (byte) 111, (byte) 240, (byte) 198, (byte) 115, (byte) 184,
        (byte) 214, (byte) 62, (byte) 101, (byte) 24, (byte) 68, (byte) 31, (byte) 221, (byte) 103,
        (byte) 16, (byte) 241, (byte) 12, (byte) 25, (byte) 236, (byte) 174, (byte) 3, (byte) 161,
        (byte) 20, (byte) 123, (byte) 169, (byte) 11, (byte) 255, (byte) 248, (byte) 163, (byte) 192,
        (byte) 162, (byte) 1, (byte) 247, (byte) 46, (byte) 188, (byte) 36, (byte) 104, (byte) 117,
        (byte) 13, (byte) 254, (byte) 186, (byte) 47, (byte) 181, (byte) 208, (byte) 218, (byte) 61,
        (byte) 20, (byte) 83, (byte) 15, (byte) 86, (byte) 179, (byte) 200, (byte) 122, (byte) 156,
        (byte) 235, (byte) 101, (byte) 72, (byte) 23, (byte) 22, (byte) 21, (byte) 159, (byte) 2,
        (byte) 204, (byte) 84, (byte) 124, (byte) 131, (byte) 0, (byte) 13, (byte) 12, (byte) 11,
        (byte) 162, (byte) 98, (byte) 168, (byte) 118, (byte) 219, (byte) 217, (byte) 237, (byte) 199,
        (byte) 197, (byte) 164, (byte) 220, (byte) 172, (byte) 133, (byte) 116, (byte) 214, (byte) 208,
        (byte) 167, (byte) 155, (byte) 174, (byte) 154, (byte) 150, (byte) 113, (byte) 102, (byte) 195,
        (byte) 99, (byte) 153, (byte) 184, (byte) 221, (byte) 115, (byte) 146, (byte) 142, (byte) 132,
        (byte) 125, (byte) 165, (byte) 94, (byte) 209, (byte) 93, (byte) 147, (byte) 177, (byte) 87,
        (byte) 81, (byte) 80, (byte) 128, (byte) 137, (byte) 82, (byte) 148, (byte) 79, (byte) 78,
        (byte) 10, (byte) 107, (byte) 188, (byte) 141, (byte) 127, (byte) 110, (byte) 71, (byte) 70,
        (byte) 65, (byte) 64, (byte) 68, (byte) 1, (byte) 17, (byte) 203, (byte) 3, (byte) 63,
        (byte) 247, (byte) 244, (byte) 225, (byte) 169, (byte) 143, (byte) 60, (byte) 58, (byte) 249,
        (byte) 251, (byte) 240, (byte) 25, (byte) 48, (byte) 130, (byte) 9, (byte) 46, (byte) 201,
        (byte) 157, (byte) 160, (byte) 134, (byte) 73, (byte) 238, (byte) 111, (byte) 77, (byte) 109,
        (byte) 196, (byte) 45, (byte) 129, (byte) 52, (byte) 37, (byte) 135, (byte) 27, (byte) 136,
        (byte) 170, (byte) 252, (byte) 6, (byte) 161, (byte) 18, (byte) 56, (byte) 253, (byte) 76,
        (byte) 66, (byte) 114, (byte) 100, (byte) 19, (byte) 55, (byte) 36, (byte) 106, (byte) 117,
        (byte) 119, (byte) 67, (byte) 255, (byte) 230, (byte) 180, (byte) 75, (byte) 54, (byte) 92,
        (byte) 228, (byte) 216, (byte) 53, (byte) 61, (byte) 69, (byte) 185, (byte) 44, (byte) 236,
        (byte) 183, (byte) 49, (byte) 43, (byte) 41, (byte) 7, (byte) 104, (byte) 163, (byte) 14,
        (byte) 105, (byte) 123, (byte) 24, (byte) 158, (byte) 33, (byte) 57, (byte) 190, (byte) 40,
        (byte) 26, (byte) 91, (byte) 120, (byte) 245, (byte) 35, (byte) 202, (byte) 42, (byte) 176,
        (byte) 175, (byte) 62, (byte) 254, (byte) 4, (byte) 140, (byte) 231, (byte) 229, (byte) 152,
        (byte) 50, (byte) 149, (byte) 211, (byte) 246, (byte) 74, (byte) 232, (byte) 166, (byte) 234,
        (byte) 233, (byte) 243, (byte) 213, (byte) 47, (byte) 112, (byte) 32, (byte) 242, (byte) 31,
        (byte) 5, (byte) 103, (byte) 173, (byte) 85, (byte) 16, (byte) 206, (byte) 205, (byte) 227,
        (byte) 39, (byte) 59, (byte) 218, (byte) 186, (byte) 215, (byte) 194, (byte) 38, (byte) 212,
        (byte) 145, (byte) 29, (byte) 210, (byte) 28, (byte) 34, (byte) 51, (byte) 248, (byte) 250,
        (byte) 241, (byte) 90, (byte) 239, (byte) 207, (byte) 144, (byte) 182, (byte) 139, (byte) 181,
        (byte) 189, (byte) 192, (byte) 191, (byte) 8, (byte) 151, (byte) 30, (byte) 108, (byte) 226,
        (byte) 97, (byte) 224, (byte) 198, (byte) 193, (byte) 89, (byte) 171, (byte) 187, (byte) 88,
        (byte) 222, (byte) 95, (byte) 223, (byte) 96, (byte) 121, (byte) 126, (byte) 178, (byte) 138,
        (byte) 71, (byte) 241, (byte) 180, (byte) 230, (byte) 11, (byte) 106, (byte) 114, (byte) 72,
        (byte) 133, (byte) 78, (byte) 158, (byte) 235, (byte) 226, (byte) 248, (byte) 148, (byte) 83,
        (byte) 224, (byte) 187, (byte) 160, (byte) 2, (byte) 232, (byte) 90, (byte) 9, (byte) 171,
        (byte) 219, (byte) 227, (byte) 186, (byte) 198, (byte) 124, (byte) 195, (byte) 16, (byte) 221,
        (byte) 57, (byte) 5, (byte) 150, (byte) 48, (byte) 245, (byte) 55, (byte) 96, (byte) 130,
        (byte) 140, (byte) 201, (byte) 19, (byte) 74, (byte) 107, (byte) 29, (byte) 243, (byte) 251,
        (byte) 143, (byte) 38, (byte) 151, (byte) 202, (byte) 145, (byte) 23, (byte) 1, (byte) 196,
        (byte) 50, (byte) 45, (byte) 110, (byte) 49, (byte) 149, (byte) 255, (byte) 217, (byte) 35,
        (byte) 209, (byte) 0, (byte) 94, (byte) 121, (byte) 220, (byte) 68, (byte) 59, (byte) 26,
        (byte) 40, (byte) 197, (byte) 97, (byte) 87, (byte) 32, (byte) 144, (byte) 61, (byte) 131,
        (byte) 185, (byte) 67, (byte) 190, (byte) 103, (byte) 210, (byte) 70, (byte) 66, (byte) 118,
        (byte) 192, (byte) 109, (byte) 91, (byte) 126, (byte) 178, (byte) 15, (byte) 22, (byte) 41,
        (byte) 60, (byte) 169, (byte) 3, (byte) 84, (byte) 13, (byte) 218, (byte) 93, (byte) 223,
        (byte) 246, (byte) 183, (byte) 199, (byte) 98, (byte) 205, (byte) 141, (byte) 6, (byte) 211,
        (byte) 105, (byte) 92, (byte) 134, (byte) 214, (byte) 20, (byte) 247, (byte) 165, (byte) 102,
        (byte) 117, (byte) 172, (byte) 177, (byte) 233, (byte) 69, (byte) 33, (byte) 112, (byte) 12,
        (byte) 135, (byte) 159, (byte) 116, (byte) 164, (byte) 34, (byte) 76, (byte) 111, (byte) 191,
        (byte) 31, (byte) 86, (byte) 170, (byte) 46, (byte) 179, (byte) 120, (byte) 51, (byte) 80,
        (byte) 176, (byte) 163, (byte) 146, (byte) 188, (byte) 207, (byte) 25, (byte) 28, (byte) 167,
        (byte) 99, (byte) 203, (byte) 30, (byte) 77, (byte) 62, (byte) 75, (byte) 27, (byte) 155,
        (byte) 79, (byte) 231, (byte) 240, (byte) 238, (byte) 173, (byte) 58, (byte) 181, (byte) 89,
        (byte) 4, (byte) 234, (byte) 64, (byte) 85, (byte) 37, (byte) 81, (byte) 229, (byte) 122,
        (byte) 137, (byte) 56, (byte) 104, (byte) 82, (byte) 123, (byte) 252, (byte) 39, (byte) 174,
        (byte) 215, (byte) 189, (byte) 250, (byte) 7, (byte) 244, (byte) 204, (byte) 142, (byte) 95,
        (byte) 239, (byte) 53, (byte) 156, (byte) 132, (byte) 43, (byte) 21, (byte) 213, (byte) 119,
        (byte) 52, (byte) 73, (byte) 182, (byte) 18, (byte) 10, (byte) 127, (byte) 113, (byte) 136,
        (byte) 253, (byte) 157, (byte) 24, (byte) 65, (byte) 125, (byte) 147, (byte) 216, (byte) 88,
        (byte) 44, (byte) 206, (byte) 254, (byte) 36, (byte) 175, (byte) 222, (byte) 184, (byte) 54,
        (byte) 200, (byte) 161, (byte) 128, (byte) 166, (byte) 153, (byte) 152, (byte) 168, (byte) 47,
        (byte) 14, (byte) 129, (byte) 101, (byte) 115, (byte) 228, (byte) 194, (byte) 162, (byte) 138,
        (byte) 212, (byte) 225, (byte) 17, (byte) 208, (byte) 8, (byte) 139, (byte) 42, (byte) 242,
        (byte) 237, (byte) 154, (byte) 100, (byte) 63, (byte) 193, (byte) 108, (byte) 249, (byte) 236
    };

    /**
     * As per the MS-PST standards document v7.2, Section 5.1
     *
     * @param data Data to encrypt/decrypt
     * @param encrypt Encrypt or decrypt (true = encrypt)
     * @return Encrypted/decrypted data
     */
    public static byte[] permute(byte[] data, boolean encrypt) {
        final byte[] mpbbR = new byte[256];
        final byte[] mpbbI = new byte[256];

        System.arraycopy(mpbbCrypt, 0, mpbbR, 0, 256);
        System.arraycopy(mpbbCrypt, 512, mpbbI, 0, 256);

        byte[] pbTable = encrypt ? mpbbR : mpbbI;
        byte[] buffer = new byte[data.length];

        System.arraycopy(data, 0, buffer, 0, data.length);

        // yes, this can (and SHOULD) be optimized!
        for (int pb = 0; pb < buffer.length; pb++) {
            buffer[pb] = pbTable[((int) data[pb]) & 0xFF];
        }

        return buffer;
    }
}
