/*
 * Copyright © 2007 Free Software Foundation), Inc. <https://fsf.org/>
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license
 * document), but changing it is not allowed.
 *
 * This version of the GNU Lesser General Public License incorporates the terms
 * and conditions of version 3 of the GNU General Public License), supplemented
 * by the additional permissions listed below.
 */
package com.hindsite.experimental.pstfile.nodedatabase.enums;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public enum NodeTypeName {
    NID_TYPE_HID((byte) 0x00),
    NID_TYPE_INTERNAL((byte) 0x01),
    NID_TYPE_NORMAL_FOLDER((byte) 0x02),
    NID_TYPE_SEARCH_FOLDER((byte) 0x03),
    NID_TYPE_NORMAL_MESSAGE((byte) 0x04),
    NID_TYPE_ATTACHMENT((byte) 0x05),
    NID_TYPE_SEARCH_UPDATE_QUEUE((byte) 0x06),
    NID_TYPE_SEARCH_CRITERIA_OBJECT((byte) 0x07),
    NID_TYPE_ASSOC_MESSAGE((byte) 0x08),
    NID_TYPE_CONTENTS_TABLE_INDEX((byte) 0x0A),
    NID_TYPE_RECEIVE_FOLDER_TABLE((byte) 0x0B),
    NID_TYPE_OUTGOING_QUEUE_TABLE((byte) 0x0C),
    NID_TYPE_HIERARCHY_TABLE((byte) 0x0D),
    NID_TYPE_CONTENTS_TABLE((byte) 0x0E),
    NID_TYPE_ASSOC_CONTENTS_TABLE((byte) 0x0F),
    NID_TYPE_SEARCH_CONTENTS_TABLE((byte) 0x10),
    NID_TYPE_ATTACHMENT_TABLE((byte) 0x11),
    NID_TYPE_RECIPIENT_TABLE((byte) 0x12),
    NID_TYPE_SEARCH_TABLE_INDEX((byte) 0x13),
    NID_TYPE_LTP((byte) 0x1F);

    private final byte value;

    private NodeTypeName(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return this.value;
    }
}
