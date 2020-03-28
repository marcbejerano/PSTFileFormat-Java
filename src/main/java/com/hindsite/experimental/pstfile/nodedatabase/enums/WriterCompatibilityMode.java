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
package com.hindsite.experimental.pstfile.nodedatabase.enums;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public enum WriterCompatibilityMode {
    Outlook2003RTM,
    Outlook2003SP3, // Will write TimeZoneDefinitionStartDisplay / EndDisplay
    Outlook2007RTM, // Do not use DList
    Outlook2007SP2, // Use DList
    Outlook2010RTM;
}
