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
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class PolarGridTest
{
    private static final String TEST_OUTPUT = "target/test-output/";
    private static String[] HEAD, TAIL; // read by beforeClass
    private PrintWriter pw;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        new File(TEST_OUTPUT).mkdirs();
        String[] lines = {};
        lines = FileUtils.readLines(new File("src/test/resources/minimal.svg"), "UTF-8").toArray(lines);
        HEAD = Arrays.copyOfRange(lines, 0, lines.length - 2);
        TAIL = Arrays.copyOfRange(lines, lines.length - 2, lines.length);
    }

    private void openPrintwriter() throws FileNotFoundException
    {
        final StackTraceElement caller = new Exception().getStackTrace()[1];
        pw = new PrintWriter(new FileOutputStream(TEST_OUTPUT + caller.getMethodName() + ".svg"));
        for (String line : HEAD)
            pw.println(line);
    }

    @After
    public void closePrintWriter()
    {
        for (String line : TAIL)
            pw.println(line);
        pw.close();
    }

    @Test
    public void concentric() throws Exception
    {
        openPrintwriter();
        new PolarGrid(45, 180, 160, 100, "#FF9999", 1.0).print(pw);
        new PolarGrid(60, 120, 103.026774, 50, "#99FF99", 1.0).print(pw);
    }

    @Test
    public void original3060() throws Exception
    {
        openPrintwriter();
        new PolarGrid(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(60, 180, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void improved3060() throws Exception
    {
        openPrintwriter();
        new PolarGridImproved(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(60, 180, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original6030() throws Exception
    {
        openPrintwriter();
        new PolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void improved6030() throws Exception
    {
        openPrintwriter();
        new PolarGridImproved(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void moreImproved6030() throws Exception
    {
        openPrintwriter();
        new PolarGridMoreImproved(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridMoreImproved(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original45() throws Exception
    {
        openPrintwriter();
        new PolarGrid(45, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(45, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void improved45() throws Exception
    {
        openPrintwriter();
        new PolarGridImproved(45, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(45, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void inscribedPolygon45() throws Exception
    {
        openPrintwriter();
        new PolarGridInscribedPolygon(45, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridInscribedPolygon(45, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original30() throws Exception
    {
        openPrintwriter();
        new PolarGrid(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void improved30() throws Exception
    {
        openPrintwriter();
        new PolarGridImproved(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void inscribedPolygon30() throws Exception
    {
        openPrintwriter();
        new PolarGridInscribedPolygon(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridInscribedPolygon(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void moreImproved30() throws Exception
    {
        openPrintwriter();
        new PolarGridMoreImproved(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridMoreImproved(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original60() throws Exception
    {
        openPrintwriter();
        new PolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void improved60() throws Exception
    {
        openPrintwriter();
        new PolarGridImproved(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridImproved(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void moreImproved60() throws Exception
    {
        openPrintwriter();
        new PolarGridMoreImproved(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridMoreImproved(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void inscribedPolygon60() throws Exception
    {
        openPrintwriter();
        new PolarGridInscribedPolygon(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGridInscribedPolygon(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }
}
