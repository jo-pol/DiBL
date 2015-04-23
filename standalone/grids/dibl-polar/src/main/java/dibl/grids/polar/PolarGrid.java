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

    private static double SCALE = (90 / 25.4); // 90 DPI / mm
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
    public PolarGrid(final int angleOnFootside, final int dotsPerCircle, final double diameter, final double minDiameter, final String fillColor,
            final double dotSize)
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

    public void print(final PrintWriter pw) throws FileNotFoundException
    {
        double alpha = Math.toRadians(360D / dotsPerCircle);
        double diameter = maxDiameter;
        int circleNr = 0; // on even circles the dots are shifted half a distance
        do
        {
            pw.println(String.format("<svg:g inkscape:label='diameter: %f mm'> ", diameter));
            createRingOfDots(pw, alpha, diameter, circleNr);
            pw.println("</svg:g>");
            // increment
            diameter -= delta(diameter, this.angleOnFootside, (double) dotsPerCircle);
            circleNr++;
        } while (diameter > minDiameter);
    }

    double delta(double diameter, int angleOnFootside, double dotsPerCircle)
    {
        double angle = Math.toRadians(angleOnFootside);
        return Math.tan(angle) * (diameter * Math.PI / dotsPerCircle);
    }

    private void createRingOfDots(final PrintWriter pw, double aRadians, double diameter, int circleNr)
    {
        for (int dotNr = 0; dotNr < dotsPerCircle; dotNr++)
        {
            final double a = (dotNr + ((circleNr % 2) * 0.5D)) * aRadians;
            final double x = (diameter / 2D) * Math.cos(a);
            final double y = (diameter / 2D) * Math.sin(a);
            pw.println(MessageFormat.format(DOT, x * SCALE, y * SCALE, fillColor, dotSize * SCALE));
        }
    }
}
