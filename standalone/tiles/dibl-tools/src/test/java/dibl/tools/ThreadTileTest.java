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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

public class ThreadTileTest
{
    @Test
    public void generate() throws IOException
    {
        final OutputStream os = new FileOutputStream("target/threadTiles.svg");
        os.write(SvgDoc.HEAD.getBytes());
        for (final Stitch s : Stitch.values())
        {
            for (final Tuple t : Tuple.list())
            {
                os.write(new ThreadTile(s, t.toString()).toString().getBytes());
            }
        }
        os.write(SvgDoc.TAIL.getBytes());
        os.close();
    }
}
