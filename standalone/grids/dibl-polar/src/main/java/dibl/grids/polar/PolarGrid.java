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

package dibl.grids.polar;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.MessageFormat;

public class PolarGrid
{
    private static final String DOT = "\t<svg:circle" + //
            " cx=\"{0}\" cy=\"{1}\"" + //
            " sodipodi:cx=\"{0}\" sodipodi:cy=\"{1}\"" + //
            " r=\"{3}\" sodipodi:rx=\"{3}\" sodipodi:ry=\"{3}\"" + //
            " style=\"fill:{2}\"/>";

    private final int angleOnFootside;
    private final int dotsPerCircle;
    private final double minDiameter;
    private final double maxDiameter;
    private final String fillColor;
    private final double dotSize;

    /**
     * @param angleOnFootside
     *        min 10 max 80 degrees. Dots are placed on alternating positions per circle. Drawing lines
     *        through the dots gives a figure like below:
     * 
     *        <pre>
     *         ________
     *          \ /\ /
     *         __V__V__
     * </pre>
     * 
     *        The angle between the outside of the V and the inner circle is given.
     * @param dotsPerCircle
     * @param diameter
     *        measured in mm.
     * @param minDiameter
     *        measured in mm. Circles of dots are generated in groups until the circles get too small.
     * @param fillColor
     */
    public PolarGrid(final int angleOnFootside, final int dotsPerCircle, final double diameter, final double minDiameter, final String fillColor, final double dotSize)
    {
        this.dotSize = dotSize;
        if (angleOnFootside < 10 || angleOnFootside > 80)
            throw new IllegalArgumentException("angle must be between 10 an 80, got " + angleOnFootside);
        if (dotsPerCircle < 4)
            throw new IllegalArgumentException("at least 4 dots, got " + dotsPerCircle);
        if (minDiameter >= diameter)
            throw new IllegalArgumentException("min > max diameter");
        if (minDiameter <= 0 || diameter <= 0 || dotSize <= 0)
            throw new IllegalArgumentException("diameters and dotSize must be positive");
        this.angleOnFootside = angleOnFootside;
        this.dotsPerCircle = dotsPerCircle;
        this.minDiameter = minDiameter;
        this.maxDiameter = diameter;
        this.fillColor = fillColor;
    }

    /**
     * Gets the distance between circles of dots.
     * 
     * @param diameter
     *        can be incremented with the result to get subsequent circles for dots
     * @result half the distance between two dots on a circle
     */
    private double getDistance(final double diameter, final int dotsPerCircle)
    {

        final double x = (diameter * Math.PI) / (double) dotsPerCircle;
        return x * Math.tan(Math.toRadians(this.angleOnFootside));
    }

    public void print(final PrintWriter pw) throws FileNotFoundException
    {
        double aRadians = Math.toRadians(360D / dotsPerCircle);
        double diameter = maxDiameter;
        int circleNr = 0; // on even circles the dots are shifted half a distance
        aRadians = Math.toRadians(360D / dotsPerCircle);
        do
        {
            final double distance = this.getDistance(diameter, dotsPerCircle);
            pw.println(String.format("<svg:g inkscape:label='%.1f mm per dot, diameter: %f mm'> ", distance, diameter));
            // create the dots on one circle
            for (int dotNr = 0; dotNr < dotsPerCircle; dotNr++)
            {
                final double a = (dotNr + ((circleNr % 2) * 0.5D)) * aRadians;
                final double x = (diameter / 2D) * Math.cos(a);
                final double y = (diameter / 2D) * Math.sin(a);
                pw.println(MessageFormat.format(DOT, x, y, fillColor, dotSize));
            }
            pw.println("</svg:g>");
            // increment
            diameter -= distance;
            circleNr++;
        } while (diameter > minDiameter);
    }
}
