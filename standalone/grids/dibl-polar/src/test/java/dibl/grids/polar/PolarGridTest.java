// @formatter:off
/*
 * Copyright 2014, J. Pol
 *
 * This file is part of free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation.
 *
 * This package is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details. A copy of the GNU General Public License is
 * available at <http://www.gnu.org/licenses/>.
 */
// @formatter:on
package dibl.grids.polar;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.junit.Test;

public class PolarGridTest
{
    private static final String HEAD = "" + //
            "<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n" + //
            "<svg\n" + //
            "   xmlns:dc='http://purl.org/dc/elements/1.1/'\n" + //
            "   xmlns:cc='http://creativecommons.org/ns#'\n" + //
            "   xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'\n" + //
            "   xmlns:svg='http://www.w3.org/2000/svg'\n" + //
            "   xmlns='http://www.w3.org/2000/svg'\n" + //
            "   xmlns:sodipodi='http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd'\n" + //
            "   xmlns:inkscape='http://www.inkscape.org/namespaces/inkscape'\n" + //
            "   id='svg2'\n" + //
            "   version='1.1'>\n" + //
            "  <g\n" + //
            "     inkscape:label='Layer 1'\n" + //
            "     inkscape:groupmode='layer'\n" + //
            "     id='layer1'>\n";
    private static final String TAIL = "  </g>\n</svg>\n";

    @Test
    public void printDefault() throws Exception
    {
        final PrintWriter pw = new PrintWriter(new FileOutputStream("target/grid.svg"));
        pw.print(HEAD);
        new PolarGrid(45,180,160,100,"#FF9999",0.2).print(pw);
        new PolarGrid(60,120,103.026774,50,"#99FF99",0.2).print(pw);
        pw.print(TAIL);
        pw.close();
    }
}
