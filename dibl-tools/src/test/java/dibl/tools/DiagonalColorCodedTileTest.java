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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DiagonalColorCodedTileTest
{
    @Test
    public void generate()
    {
        for (ColorCodedTile.Stitch stitch : ColorCodedTile.Stitch.values())
        {
            String string = DiagonalColorCodedTile.generate(stitch);
            System.out.println(string);
            assertThat(string.split("\n").length, is(69 * (stitch.hasPin()?5:4)));
        }
    }
}
