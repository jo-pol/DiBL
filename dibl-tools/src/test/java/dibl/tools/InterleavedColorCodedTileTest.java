// @formatter:off
/*
 * Copyright 2013, J. Pol
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
package dibl.tools;

import static dibl.tools.Stitch.tc;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class InterleavedColorCodedTileTest
{
    @Test
    public void verify()
    {
        final String attributes = "style=\"fill:none;stroke:#00ff00\" inkscape:connector-curvature=\"0\" sodipodi:nodetypes=\"cc\"";
        final String expected = "<g inkscape:label=\"tc (0,1,0,0,1,0,-1,-1)\">\n" + //
                "<path d=\"M 36,0 C 28,8 18,24 18,36 \" " + attributes + " />\n" + //
                "<path d=\"M 0,18 C 12,18 28,28 36,36 \" " + attributes + " />\n" + //
                "</g>\n";
        assertThat(new InterleavedColorCodedTile(tc, "(0,1,0,0,1,0,-1,-1)").toString(), is(expected));
    }

    @Test
    public void generate()
    {
        for (final Stitch s : Stitch.values())
        {
            for (final Tuple t : Tuple.list())
            {
                System.out.println(new InterleavedColorCodedTile(s, t.toString()));
            }
        }
    }
}
