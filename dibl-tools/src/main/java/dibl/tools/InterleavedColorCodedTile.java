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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InterleavedColorCodedTile extends ColorCodedTile
{
    private static final List<String> IN = Collections.unmodifiableList(Arrays.asList(//
            "36,18 C 24,18", "36,0 C 28,8", "18,0 C 18,12", "0,0 C 8,8", "0,18 C 12,18", "-", "-", "-"));
    private static final List<String> OUT = Collections.unmodifiableList(Arrays.asList(//
            "24,18 36,18", "-", "-", "-", "12,18 0,18", "8,28 0,36", "18,24 18,36", "28,28 36,36"));

    public InterleavedColorCodedTile(final Stitch stitch, final String tuple)
    {
        super(stitch, tuple);
    }

    public String getIn(final int i)
    {
        return IN.get(i);
    }

    public String getOut(final int i)
    {
        return OUT.get(i);
    }

    public static String generate(final Stitch stitch)
    {
        final StringBuffer sb = new StringBuffer();
        final int max = Integer.parseInt("22222222", 3);
        for (int i = 0; i <= max; i++)
        {
            ColorCodedTile tile;
            try
            {
                tile = new InterleavedColorCodedTile(stitch, toTuple(Integer.toString(i, 3)));
            }
            catch (final IllegalArgumentException e)
            {
                continue;
            }
            sb.append(tile.toString());
        }
        return sb.toString();
    }
}
