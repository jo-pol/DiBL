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

import java.text.MessageFormat;

public class FrameForThreadTile extends InterleavedColorCodedTile
{
    public FrameForThreadTile(final Stitch stitch, final String tuple)
    {
        super(stitch, tuple);
    }

    public FrameForThreadTile newInstance(final Stitch stitch, final String tuple)
    {
        return new FrameForThreadTile(stitch, tuple);
    }

    public String toString()
    {
        final String line1 = createSimpleLine(firstIn(), lastOut());
        final String line2 = createSimpleLine(lastIn(), firstOut());
        return MessageFormat.format(TILE, stitch.name(), tuple, line1, line2, "");
    }

    private String createSimpleLine(final int in, final int out)
    {
        return MessageFormat.format(LINE, "000000;stroke-width:8", getIn(in), getOut(out), "", "cc");
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
                tile = new FrameForThreadTile(stitch, toTuple(Integer.toString(i, 3)));
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
