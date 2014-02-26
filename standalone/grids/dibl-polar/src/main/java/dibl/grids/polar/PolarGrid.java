/*
 * PolarGridModel.java Copyright 2005-2007 by J. Pol This file is part free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option) any later version. Th
 * is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details. You should have received a copy of the GNU General Public License along with
 * BobbinWork. If not, see <http://www.gnu.org/licenses/>.
 */

package dibl.grids.polar;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.MessageFormat;

/**
 * @author J. Pol
 */
public class PolarGrid
{
    private static final String HEAD = "" + //
            "<?xml version='1.0' encoding='UTF-8' standalone='no'?>\n" + //
            "<svg\n" + //
            "   xmlns:dc='http://purl.org/dc/elements/1.1/'\n" + //
            "   xmlns:cc='http://creativecommons.org/ns#'\n" + //
            "   xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'\n" + //
            "   xmlns:svg='http://www.w3.org/2000/svg'\n" + //
            "   xmlns='http://www.w3.org/2000/svg'\n" + //
            "   xmlns:sodipodi='http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd'\n" + //
            "   xmlns:inkscape='http://www.inkscape.org/namespaces/inkscape'\n" + //
            "   id='svg2'\n" + //
            "   version='1.1'>\n" + //
            "  <g\n" + //
            "     inkscape:label='Layer 1'\n" + //
            "     inkscape:groupmode='layer'\n" + //
            "     id='layer1'>\n";
    private static final String FORMAT="<svg:circle" + //
    		" cx=\"{0}\" cy=\"{1}\"" + //
    		" sodipodi:cx=\"{0}\" sodipodi:cy=\"{1}\"" + //
    		" r=\"0.2\" sodipodi:rx=\"0.2\" sodipodi:ry=\"0.2\"" + //
    		" style=\"fill:#999999\"/>";
    private static final String TAIL = "  </g>\n</svg>\n";

    private static final int MIN_ANGLE = 10;
    private static final int MAX_ANGLE = 80;
    private static final int MIN_REPEATS = 2;
    private static final int MAX_REPEATS = 200; // otherwise out of memory
    private static final int MIN_DOTS_PER_REPEAT = 2;
    private static final int MAX_DOTS_PER_REPEAT = 200; // otherwise out of memory
    private static final double MIN_DIAMETER = 0.4D;
    private static final double SCALE_MM = 2 * 1.7716536D; // mm on paper

    /**
     * Dots are placed on alternating positions per circle. Drawing lines through the dots gives a figure
     * like below:
     * 
     * <pre>
     * ________
     *  \ /\ /
     * __V__V__
     * </pre>
     * 
     * The angle between the outside of the V and the inner circle is given.
     */
    int angleOnFootside = 45; // degrees
    int dotsPerCircle = 200;
    double minDiameter = 50;
    double maxDiameter = 75;

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

    public void printDoc(PrintWriter pw) throws FileNotFoundException
    {
        pw.print(HEAD);
        double aRadians = Math.toRadians(360D / dotsPerCircle);
        double diameter = minDiameter;
        int circleNr = 0; // on even circles the dots are shifted half a distance
        aRadians = Math.toRadians(360D / dotsPerCircle);
        do
        {
            // create the dots on one circle
            for (int dotNr = 0; dotNr < dotsPerCircle; dotNr++)
            {
                final double a = (dotNr + ((circleNr % 2) * 0.5D)) * aRadians;
                double x = (diameter / 2D) * Math.cos(a);
                double y = (diameter / 2D) * Math.sin(a);
                pw.println(MessageFormat.format(FORMAT, x * SCALE_MM,y * SCALE_MM));
            }
            // increment
            diameter += this.getDistance(diameter, dotsPerCircle);
            circleNr++;
        } while (diameter < maxDiameter);
        pw.print(TAIL);
    }
}
