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
public interface BlockType {

    byte XBlock = 0x01;     // XBlock and XXBlock has the same btype and different cLevel
    byte XXBlock = 0x01;    // XBlock and XXBlock has the same btype and different cLevel

    byte SLBLOCK = 0x02;    // SLBLOCK and SIBLOCK has the same btype and different cLevel
    byte SIBLOCK = 0x02;    // SLBLOCK and SIBLOCK has the same btype and different cLevel

}
