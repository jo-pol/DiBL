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

/**
 * A tile containing a thread diagram of stitch. A stitch consist of two pairs of lines starting at the
 * upper half of a tile, ending at the bottom half of the tile. End points of the lines are around the
 * corners of the tile or around the center of the edges.<br>
 * A sketch for the nodes of the primary lines to draw:<br>
 * <br>
 * <img src="ThreadTileCoordinates.svg">
 */
public class ThreadTile
{
    private static final String[] INR = {"21,4 C 12,4", "24,-19 C 14,-12", "-16,3 C -10,4", "-19,-24 C -11,-16", "-21,-4 C -12,-4", "-", "-", "-"};
    private static final String[] OUTR = {"6,-3 15,-3", "-", "-", "-", "-6,3 -15,3", "-5,9 -12,17", "4,4 16,3", "11,4 17,12"};
    private static final String[] INL = {"21,-4 C 12,-4", "19,-24 C 11,-16", "-16,-3 C -10,-4", "-24,-19 C -14, -12", "-21,4 C -12,4", "-", "-", "-"};
    private static final String[] OUTL = {"6,3 15,3", "-", "-", "-", "-6,-3 -15,3", "-11,4 -17,12", "4,-4 16,-3", "5,9 12,17"};
    private static final String BLACK = "000000";

    final Stitch stitch;
    final Tuple tuple;

    public ThreadTile(final Stitch stitch, final String tuple)
    {
        this.stitch = stitch;
        this.tuple = new Tuple(tuple);
    }

    public String toString()
    {
        final String line1 = new Line(BLACK, INL[tuple.firstIn()], OUTL[tuple.firstOut()]).toString();
        final String line2 = new Line("FF0000", INR[tuple.firstIn()], OUTR[tuple.firstOut()]).toString();
        final String line3 = new Line("00FF00", INL[tuple.lastIn()], OUTL[tuple.lastOut()]).toString();
        final String line4 = new Line("0000FF", INR[tuple.lastIn()], OUTR[tuple.lastOut()]).toString();
        /** TODO apply {@link Stitch#sectionsLeft} */
        return new Tile(stitch, tuple, line1, line2, line3, line4).toString();
    }
}
