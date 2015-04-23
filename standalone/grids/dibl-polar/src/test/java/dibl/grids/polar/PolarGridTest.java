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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class PolarGridTest {
    private static final String TEST_OUTPUT = "target/test-output/";
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
    private static final String TAIL = "  </g>\n</svg>\n";
    private PrintWriter pw;

    private class PolarGridImproved extends PolarGrid {

        public PolarGridImproved(int angleOnFootside, int dotsPerCircle, double diameter, double minDiameter, String fillColor, double dotSize) {
            super(angleOnFootside, dotsPerCircle, diameter, minDiameter, fillColor, dotSize);
        }

        @Override
        double delta(double diameter, int angleOnFootside, double dotsPerCircle) {
            double angle = Math.toRadians(angleOnFootside);
            double alpha = Math.toRadians(360D / dotsPerCircle);
            double correction = alpha / (360.0 / angleOnFootside);
            return Math.tan(angle - correction) * (diameter * Math.PI / dotsPerCircle);
        }
    }

    private class PolarGridMoreImproved extends PolarGrid {

        public PolarGridMoreImproved(int angleOnFootside, int dotsPerCircle, double diameter, double minDiameter, String fillColor, double dotSize) {
            super(angleOnFootside, dotsPerCircle, diameter, minDiameter, fillColor, dotSize);
        }

        @Override
        double delta(double diameter, int angleOnFootside, double dotsPerCircle) {
            double angle = Math.toRadians(angleOnFootside);
            double alpha = Math.toRadians(360D / dotsPerCircle);
            double correction = (alpha / (360.0 / angleOnFootside)) / (45.0 / angleOnFootside);
            return Math.tan(angle - correction) * (diameter * Math.PI / dotsPerCircle);
        }
    }

    @BeforeClass
    public static void createFolder() {
        new File(TEST_OUTPUT).mkdirs();
    }

    private void openPrintwriter() throws FileNotFoundException {
        final StackTraceElement caller = new Exception().getStackTrace()[1];
        pw = new PrintWriter(new FileOutputStream(TEST_OUTPUT + caller.getMethodName() + ".svg"));
        pw.print(HEAD);
    }

    @After
    public void closePrintWriter() {
        pw.print(TAIL);
        pw.close();
    }

    @Test
    public void concentric() throws Exception {
        openPrintwriter();
        new PolarGrid(45, 180, 160, 100, "#FF9999", 1.0).print(pw);
        new PolarGrid(60, 120, 103.026774, 50, "#99FF99", 1.0).print(pw);
    }

    @Test
    public void original3060() throws Exception {
        openPrintwriter();
        new PolarGrid(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(60, 180, 640, 400, "#FFFF00", 1.0).print(pw);
    }
    
    @Test
    public void improved3060() throws Exception {
        openPrintwriter();
        new PolarGridImproved(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(60, 180, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original6030() throws Exception {
        openPrintwriter();
        new PolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }
    
    @Test
    public void improved6030() throws Exception {
        openPrintwriter();
        new PolarGridImproved(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }
    
    @Test
    public void moreImproved6030() throws Exception {
        openPrintwriter();
        new PolarGridMoreImproved(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridMoreImproved(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original45() throws Exception {
        openPrintwriter();
        new PolarGrid(45, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(45, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void improved45() throws Exception {
        openPrintwriter();
        new PolarGridImproved(45, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(45, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original30() throws Exception {
        openPrintwriter();
        new PolarGrid(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void improved30() throws Exception {
        openPrintwriter();
        new PolarGridImproved(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void moreImproved30() throws Exception {
        openPrintwriter();
        new PolarGridMoreImproved(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridMoreImproved(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original60() throws Exception {
        openPrintwriter();
        new PolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void improved60() throws Exception {
        openPrintwriter();
        new PolarGridImproved(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void moreImproved60() throws Exception {
        openPrintwriter();
        new PolarGridMoreImproved(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridMoreImproved(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }
}
