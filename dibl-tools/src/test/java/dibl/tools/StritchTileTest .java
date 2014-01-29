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

import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;

public class StitchTileTest
{
    @Test
    public void generate() throws IOException
    {
        final OutputStream os = new FileOutputStream ("target/threadTiles.svg");
        os.open();
        os.write(SvgDoc.HEAD);
        for (final Stitch s : Stitch.values())
        {
            for (final Tuple t : Tuple.list())
            {
                os.write(new StitchTile(s, t.toString()).getBytes());
            }
        }
        os.write(SvgDoc.TAIL);
        os.close();
    }
}
