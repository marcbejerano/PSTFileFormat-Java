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
public enum InternalNodeName {

    NID_MESSAGE_STORE(0x0021),
    NID_NAME_TO_ID_MAP(0x0061),
    NID_NORMAL_FOLDER_TEMPLATE(0x00A1),
    NID_SEARCH_FOLDER_TEMPLATE(0x00C1),
    NID_ROOT_FOLDER(0x0122),
    NID_SEARCH_MANAGEMENT_QUEUE(0x01E1),
    //NID_SEARCH_ACTIVITY_LIST(0x0201), // a.k.a. SAL
    NID_SEARCH_DOMAIN_OBJECT(0x0261),
    NID_HIERARCHY_TABLE_TEMPLATE(0x060D),
    NID_CONTENTS_TABLE_TEMPLATE(0x060E),
    NID_ASSOC_CONTENTS_TABLE_TEMPLATE(0x060F),
    NID_SEARCH_CONTENTS_TABLE_TEMPLATE(0x0610),
    NID_ATTACHMENT_TABLE(0x0671),
    NID_RECIPIENT_TABLE(0x0692),
    // NID_SEARCH_ROOT_SEARCH_FOLDER(0x0723), // GUST PC?
    // NID_SEARCH_UPDATE_QUEUE(0x0726),
    // NID_SEARCH_CRITERIA_OBJECT(0x0727),
    // NID_SEARCH_ROOT_SEARCH_CONTENT_TABLE(0x0730), // GUST TC?
    NID_TOP_OF_PERSONAL_FOLDERS(0x8022),
    //NID_TOP_OF_PERSONAL_FOLDERS_HIERARCHY_TABLE(0x802D),
    //NID_SEARCH_FOLDER_HIERARCHY_TABLE(0x804D),

    // OST file
    OST_NID_ROOT_PUBLIC(0x2002), // child of NID_ROOT_FOLDER
    OST_NID_ROOT_MAILBOX(0x20A2), // child of NID_ROOT_FOLDER
    OST_NID_TOP_OF_PERSONAL_FOLDERS(0x2142); // child of OST_NID_ROOT_MAILBOX

    private final int value;

    private InternalNodeName(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
