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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ColorCodedTile
{
    enum Stitch
    {
        tc("00ff00"), ctc("cc88ff"), tctc("ff0000"), tcptc("00ff00"), ctcpctc("cc88ff"), tctcptctc("ff0000");

        private String color;

        Stitch(final String color)
        {
            this.color = color;
        }

        boolean hasPin()
        {
            return name().contains("p");
        }
    }

    static final String TILE = "<g inkscape:label=\"{0} {1}\">\n{2}\n{3}{4}\n</g>\n";
    static final String LINE = "<path d=\"M {1} {3} {2}\" style=\"fill:none;stroke:#{0}\"" + //
            " inkscape:connector-curvature=\"0\" sodipodi:nodetypes=\"{4}\" />";
    private static final List<String> MIRROR = Collections.unmodifiableList(Arrays.asList(//
            "6,18", "6,30", "18,30", "30,30", "30,18", "30,6", "18,6", "6,6"));
    private static final String PIN = "\n<path d='m 20,18 a 2,2 0 1 1 -4,0 2,2 0 1 1 4,0 z' " + //
            "sodipodi:cx='18' sodipodi:cy='18' " + //
            "sodipodi:rx='2' sodipodi:ry='2' " + //
            "style='fill:#000000;fill-opacity:1;' " + //
            "sodipodi:type='arc' " + //
            "/>";

    private final Stitch stitch;
    private final List<Integer> tupleList = new ArrayList<Integer>();
    private final String tuple;

    public ColorCodedTile(final Stitch stitch, final String tuple)
    {
        this.stitch = stitch;
        this.tuple = tuple;

        for (final String i : tuple.replaceAll("[()]", "").trim().split(","))
            tupleList.add(Integer.parseInt(i.trim()));
        tupleList.add(tupleList.get(0));

        if (nrOfZeros() != 4 || firstIn() == -1 || firstOut() == -1 || lastIn() == -1 || lastOut() == -1)
            throw new IllegalArgumentException(tuple);
    }

    private int nrOfZeros()
    {
        int count = 0;
        for (int i = 0; i < 8; i++)
            if (tupleList.get(i) == 0)
                count++;
        return count;
    }

    public String toString()
    {
        final String pin;
        final String line1;
        final String line2;
        if (stitch.hasPin())
        {
            pin = PIN;
            line1 = createLine(firstIn(), lastOut());
            line2 = createLine(lastIn(), firstOut());
            // TODO fix straight lines and lines along the same side of the pin
            // (-1, 1, 1, 0, 0, 0, 0, -1) (1, 1, 0, 0, 0, 0, -1, -1) look like ((
            // (0, 0, 1, 1, -1, -1, 0, 0) (0, 0, 0, 1, 1, -1, -1, 0) look like ))
        }
        else
        {
            pin = "";
            line1 = createSimpleLine(firstIn(), firstOut());
            line2 = createSimpleLine(lastIn(), lastOut());
        }
        return MessageFormat.format(TILE, stitch.name(), tuple, line1, line2, pin);
    }

    private String createSimpleLine(final int in, final int out)
    {
        return MessageFormat.format(LINE, stitch.color, getIn(in), getOut(out), "", "cc");
    }

    private String createLine(final int in, final int out)
    {
        Point2D start = createPoint(MIRROR.get(out));
        Point2D end = createPoint(MIRROR.get(in));
        Point2D p = new Point2D.Double((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
        String tangent = MIRROR.get(out) + " " + p.getX() + "," + p.getY() + " " + MIRROR.get(in);
        return MessageFormat.format(LINE, stitch.color, getIn(in), getOut(out), tangent, "csc");
    }

    private Point2D.Double createPoint(final String coordinates)
    {
        final String[] strings = coordinates.split(",");
        final double x = Double.parseDouble(strings[0]);
        final double y = Double.parseDouble(strings[1]);
        return new Point2D.Double(x, y);
    }

    private int firstIn()
    {
        for (int i = 0; i < 5; i++)
            if (tupleList.get(i) > 0)
                return i;
        return -1;
    }

    private int lastIn()
    {
        for (int i = firstIn() + 1; i < 5; i++)
            if (tupleList.get(i) > 0)
                return i;
        return -1;
    }

    private int firstOut()
    {
        for (int i = 4; i < 9; i++)
            if (tupleList.get(i) < 0)
                return i;
        return -1;
    }

    private int lastOut()
    {
        for (int i = firstOut() + 1; i < 9; i++)
            if (tupleList.get(i) < 0)
                return i % 8;
        return -1;
    }

    protected static String toTuple(final String s)
    {
        final String padded = ("00000000" + s).substring(s.length());
        final int[] items = new int[8];
        for (int i = 0; i < 8; i++)
        {
            items[i] = Integer.parseInt(padded.substring(i, i + 1)) - 1;
        }
        return Arrays.toString(items).replace("[", "(").replace("]", ")").replaceAll(" ", "");
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
                tile = new DiagonalColorCodedTile(stitch, toTuple(Integer.toString(i, 3)));
            }
            catch (final IllegalArgumentException e)
            {
                continue;
            }
            sb.append(tile.toString());
        }
        return sb.toString();
    }

    protected abstract newInstance(final Stitch stitch, final String tuple);

    protected abstract String getIn(int i);

    protected abstract String getOut(int i);
}
