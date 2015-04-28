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
    public void original6030() throws Exception
    {
        openPrintwriter();
        new PolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void matching6030() throws Exception
    {
        openPrintwriter();
        new MatchingPolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new MatchingPolarGrid(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original45() throws Exception
    {
        openPrintwriter();
        new PolarGrid(45, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(45, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }
    
    @Test
    public void matching45() throws Exception
    {
        openPrintwriter();
        new MatchingPolarGrid(45, 180, 640, 400, "#000000", 1.0).print(pw);
        new MatchingPolarGrid(45, 90, 640, 400, "#00FF00", 1.0).print(pw);
        new MatchingPolarGrid(45, 180*2/3, 640, 400, "#FF0000", 1.0).print(pw);
        new MatchingPolarGrid(45, 45, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void alternative45() throws Exception
    {
        openPrintwriter();
        new AlternativePolarGrid(45, 180, 640, 400, "#000000", 1.0).print(pw);
        new AlternativePolarGrid(45, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original30() throws Exception
    {
        openPrintwriter();
        new PolarGrid(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void alternative30() throws Exception
    {
        openPrintwriter();
        new AlternativePolarGrid(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new AlternativePolarGrid(30, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void matching30() throws Exception
    {
        openPrintwriter();
        new MatchingPolarGrid(30, 180, 640, 400, "#000000", 1.0).print(pw);
        new MatchingPolarGrid(30, 90, 640, 400, "#00FF00", 1.0).print(pw);
        new MatchingPolarGrid(30, 180*2/3, 640, 400, "#FF0000", 1.0).print(pw);
        new MatchingPolarGrid(30, 45, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void original60() throws Exception
    {
        openPrintwriter();
        new PolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new PolarGrid(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void matching60() throws Exception
    {
        openPrintwriter();
        new MatchingPolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new MatchingPolarGrid(60, 90, 640, 400, "#00FF00", 1.0).print(pw);
        new MatchingPolarGrid(60, 180*2/3, 640, 400, "#FF0000", 1.0).print(pw);
        new MatchingPolarGrid(60, 45, 640, 400, "#FFFF00", 1.0).print(pw);
    }

    @Test
    public void alternative60() throws Exception
    {
        openPrintwriter();
        new AlternativePolarGrid(60, 180, 640, 400, "#000000", 1.0).print(pw);
        new AlternativePolarGrid(60, 90, 640, 400, "#FFFF00", 1.0).print(pw);
    }
}
