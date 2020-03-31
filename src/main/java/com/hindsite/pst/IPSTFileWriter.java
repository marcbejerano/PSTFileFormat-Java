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

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author marcb
 */
public interface IPSTFileWriter {
    
    IPSTFileWriter write(OutputStream out) throws IOException;
}
