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
package dibl.tools;

import org.junit.Test;

public class DiagonalColorCodedTileTest
{
    @Test
    public void generate()
    {
        for (final Stitch s : Stitch.values())
        {
            for (final Tuple t : Tuple.list())
            {
                System.out.println(new DiagonalColorCodedTile(s, t.toString()));
            }
        }
    }
}
