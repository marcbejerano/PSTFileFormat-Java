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
package com.hindsite.pst.ndb;

import com.hindsite.pst.IPSTFileReader;
import com.hindsite.pst.IPSTFileWriter;
import com.hindsite.pst.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2.2.2.1
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class NodeID implements IPSTFileReader, IPSTFileWriter {
    
    public static final int size = 4;

    public static enum Type {
        NID_TYPE_HID(0x00), // Heap node
        NID_TYPE_INTERNAL(0x01), // Internal node (section 2.4.1)
        NID_TYPE_NORMAL_FOLDER(0x02), // Normal Folder object (PC)
        NID_TYPE_SEARCH_FOLDER(0x03), // Search Folder object (PC)
        NID_TYPE_NORMAL_MESSAGE(0x04), // Normal Message object (PC)
        NID_TYPE_ATTACHMENT(0x05), // Attachment object (PC)
        NID_TYPE_SEARCH_UPDATE_QUEUE(0x06), // Queue of changed objects for search Folder objects
        NID_TYPE_SEARCH_CRITERIA_OBJECT(0x07), // Defines the search criteria for a search Folder object
        NID_TYPE_ASSOC_MESSAGE(0x08), // Folder associated information (FAI) Message object (PC)
        NID_TYPE_CONTENTS_TABLE_INDEX(0x0A), // Internal, persisted view-related
        NID_TYPE_RECEIVE_FOLDER_TABLE(0x0B), // Receive Folder object (Inbox)
        NID_TYPE_OUTGOING_QUEUE_TABLE(0x0C), // Outbound queue (Outbox)
        NID_TYPE_HIERARCHY_TABLE(0x0D), // Hierarchy table (TC)
        NID_TYPE_CONTENTS_TABLE(0x0E), // Contents table (TC)
        NID_TYPE_ASSOC_CONTENTS_TABLE(0x0F), // FAI contents table (TC)
        NID_TYPE_SEARCH_CONTENTS_TABLE(0x10), // Contents table (TC) of a search Folder object
        NID_TYPE_ATTACHMENT_TABLE(0x11), // Attachment table (TC)
        NID_TYPE_RECIPIENT_TABLE(0x12), // Recipient table (TC)
        NID_TYPE_SEARCH_TABLE_INDEX(0x13), // Internal, persisted view-related
        NID_TYPE_LTP(0x1F); // LTP

        private final int value;

        private Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static Type valueOf(int value) {
            for (Type t : Type.values()) {
                if (t.getValue() == value) {
                    return t;
                }
            }
            throw new EnumConstantNotPresentException(Type.class, "Unknown value: " + Integer.toHexString(value));
        }
    }

    private Type nidType;
    private int nidIndex;

    public NodeID(InputStream in) throws IOException {
        read(in);
    }
    
    @Override
    public IPSTFileWriter write(OutputStream out) throws IOException {
        int value = (this.nidType.getValue() & 0x1F) << 27 | (this.nidIndex & 0x07FFFFFF);
        StreamUtils.write(out, value);
        return this;
    }

    @Override
    public IPSTFileReader read(InputStream in) throws IOException {
        int value = StreamUtils.readInt(in);
        this.nidType = Type.valueOf(value >> 27);
        this.nidIndex = value & 0x07FFFFFF;
        return this;
    }

}
