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

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A tile containing a single color coded stitch. A color coded stitch consist of two lines starting at
 * the upper half of a tile, ending at the bottom half of the tile. End points of the lines are at the
 * corners of the tile or in the center of the edges. Control points for the end nodes are on a
 * four-pointed star inside the tile. Lines may have an additional smooth node to guide it around the
 * pin. Control points for this additional node are on a quadrant slightly larger than the star. The
 * rules for the additional node are too simple and lead to straight lines through the pin and
 * occasionally both lines along the same side of the pin.<br>
 * A sketch for the nodes of the lines to draw:<br>
 * <br>
 * <img src="ColorCodedCoordinates.svg">
 */
public abstract class ColorCodedTile
{
    private static final List<String> MIRROR = Collections.unmodifiableList(Arrays.asList(//
            "6,18", "6,30", "18,30", "30,30", "30,18", "30,6", "18,6", "6,6"));
    private static final String PIN = "\n<path d='m 20,18 a 2,2 0 1 1 -4,0 2,2 0 1 1 4,0 z' " + //
            "sodipodi:cx='18' sodipodi:cy='18' " + //
            "sodipodi:rx='2' sodipodi:ry='2' " + //
            "style='fill:#000000;fill-opacity:1;' " + //
            "sodipodi:type='arc' " + //
            "/>";

    final Stitch stitch;
    final Tuple tuple;

    public ColorCodedTile(final Stitch stitch, final String tuple)
    {
        this.stitch = stitch;
        this.tuple = new Tuple(tuple);
    }

    public String toString()
    {
        final String line1;
        final String line2;
        if (stitch.hasPin())
        {
            line1 = createLine(tuple.firstIn(), tuple.lastOut());
            line2 = createLine(tuple.lastIn(), tuple.firstOut());
            // TODO fix straight lines and lines along the same side of the pin
            // (-1, 1, 1, 0, 0, 0, 0, -1) (1, 1, 0, 0, 0, 0, -1, -1) look like ((
            // (0, 0, 1, 1, -1, -1, 0, 0) (0, 0, 0, 1, 1, -1, -1, 0) look like ))
            return new Tile(stitch, tuple, line1, line2, PIN).toString();
        }
        else
        {
            line1 = new Line(stitch.color, getIn(tuple.firstIn()), getOut(tuple.firstOut())).toString();
            line2 = new Line(stitch.color, getIn(tuple.lastIn()), getOut(tuple.lastOut())).toString();
            return new Tile(stitch, tuple, line1, line2).toString();
        }
    }

    private String createLine(final int in, final int out)
    {
        final Point2D start = createPoint(MIRROR.get(out));
        final Point2D end = createPoint(MIRROR.get(in));
        final Point2D p = new Point2D.Double((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
        final String tangent = MIRROR.get(out) + " " + p.getX() + "," + p.getY() + " " + MIRROR.get(in);
        return new Line(stitch.color, getIn(in), tangent, getOut(out)).toString();
    }

    private Point2D.Double createPoint(final String coordinates)
    {
        final String[] strings = coordinates.split(",");
        final double x = Double.parseDouble(strings[0]);
        final double y = Double.parseDouble(strings[1]);
        return new Point2D.Double(x, y);
    }

    protected abstract String getIn(int i);

    protected abstract String getOut(int i);
}
