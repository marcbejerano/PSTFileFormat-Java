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
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public enum Property {

    PidTagNameidBucketCount(0x0001, "PtypInteger32"),
    PidTagNameidStreamGuid(0x0002, "PtypBinary"),
    PidTagNameidStreamEntry(0x0003, "PtypBinary"),
    PidTagNameidStreamString(0x0004, "PtypBinary"),
    PidTagNameidBucketBase(0x1000, "PtypBinary"),
    PidTagItemTemporaryFlags(0x1097, "PtypInteger32"),
    PidTagPstBestBodyProptag(0x661D, "PtypInteger32"),
    PidTagPstHiddenCount(0x6635, "PtypInteger32"),
    PidTagPstHiddenUnread(0x6636, "PtypInteger32"),
    PidTagPstIpmsubTreeDescendant(0x6705, "PtypBoolean"),
    PidTagPstSubTreeContainer(0x6772, "PtypInteger32"),
    PidTagLtpParentNid(0x67F1, "PtypInteger32"),
    PidTagLtpRowId(0x67F2, "PtypInteger32"),
    PidTagLtpRowVer(0x67F3, "PtypInteger32"),
    PidTagPstPassword(0x67FF, "PtypInteger32"),
    PidTagMapiFormComposeCommand(0x682F, "PtypString");

    private final int value;
    private final String type;

    private Property(int value, String type) {
        this.value = value;
        this.type = type;
    }
    
    public int getValue() { return this.value; }
    public String getType() { return this.type; }
}
