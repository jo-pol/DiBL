// @formatter:off
/*
 * Copyright 2013, J. Pol
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
package dibl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainTest
{
    private static final String DIAMOND_PATTERNS = "src/main/assembly/input/PairTraversal/diamond/";
    private static final String BRICK_PATTERNS = "src/main/assembly/input/PairTraversal/brick/";
    private static final String STITCHES = "4;4\n" + "tcptc;tc;tcptc;tc\n" + "tc;tcptc;tc;tcptc\n" + "tcptc;tc;tcptc;tc\n" + "tc;tcptc;tc;tcptc\n";
    private PrintStream savedOut;
    private InputStream savedIn;

    @Test
    public void noArgs() throws Exception
    {
        Main.main();
    }

    @Test
    public void help() throws Exception
    {
        Main.main("-help");
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongHelp() throws Exception
    {
        Main.main("help");
    }

    @Test
    public void tooManyArgs() throws Exception
    {
        Main.main("","","");
    }

    @Test
    public void flipWithoutTuples() throws Exception
    {
        Main.main("-x","");
    }

    @Test
    public void mixedFlips() throws Exception
    {
        Main.main("-x","-Y","","");
    }

    @Test
    public void inlineSingleArg() throws Exception
    {
        System.setIn(new FileInputStream(DIAMOND_PATTERNS + "3x3.svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/main1.png")));
        Main.main("-ext", "png", STITCHES);
    }

    @Test
    public void inlineTwoArgs() throws Exception
    {
        System.setIn(new FileInputStream(DIAMOND_PATTERNS + "3x3.svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/main2.svg")));
        Main.main(STITCHES, "3\t3\n" //
                + "(0,1,1,0,-1,-1)\t(1,0,1,0,-1,-1)\t(0,0,1,1,-1,-1)\n" //
                + "(0,1,1,-1,-1,0)\t(-1,1,0,1,0,-1)\t(0,1,1,0,-1,-1)\n" //
                + "(-1,1,1,0,-1,0)\t(0,0,0,0,0,0)\t(1,1,0,-1,0,-1)");
    }

    @Test
    public void hybrid() throws Exception
    {
        System.setIn(new FileInputStream(DIAMOND_PATTERNS + "4x4.svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/mainRotated.png")));
        Main.main("-x", "-y", "-ext", "png", STITCHES, DIAMOND_PATTERNS + "4x4/4x4_2.txt");
    }

    @Test
    public void hybridDiamondX() throws Exception
    {
        System.setIn(new FileInputStream(DIAMOND_PATTERNS + "3x3.svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/mainDiamondFlippedAlongX.jpg")));
        Main.main("-x", "-ext", "jpg", STITCHES, DIAMOND_PATTERNS + "3x3/3x3_3.txt");
    }

    @Test
    public void hybridDiamondY() throws Exception
    {

        System.setIn(new FileInputStream(DIAMOND_PATTERNS + "3x3.svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/mainDiamondFlippedAlongY.tiff")));
        Main.main("-y", "-ext", "tiff", STITCHES, DIAMOND_PATTERNS + "3x3/3x3_4.txt");
    }

    @Test
    public void brick() throws Exception
    {
        new File("target/brick").mkdirs();
        loop("3x3", 8);
        loop("4x4", 8/*199*/);// even 2x8 takes about a minute
    }

    private void loop(final String dimensions, final int n) throws Exception
    {
        final String extension = "png";
        for (int i = 1; i < n; i++)
        {
            brick(dimensions, i, "", extension);
            brick(dimensions, i, "-X", extension);
            brick(dimensions, i, "-Y", extension);
        }
    }

    private void brick(final String dimensions, final int n, final String flip, final String extension) throws Exception
    {
//        final String stitches = "4;4\n" + "-tc;-tc;-tc;-tc\n" + "-tc;-tc;-tc;-tc\n" + "-tc;-tc;-tc;-tc\n" + "-tc;-tc;-tc;-tc\n";
        final String stitches = "4;4\n" + "tcptc;tcptc;tcptc;tcptc\n" + "tcptc;tcptc;tcptc;tcptc\n" + "tcptc;tcptc;tcptc;tcptc\n" + "tcptc;tcptc;tcptc;tcptc\n";
        System.setIn(new FileInputStream(BRICK_PATTERNS + dimensions + ".svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/brick/" + dimensions + "_" + n + flip + ".png")));
        final String pattern = BRICK_PATTERNS + dimensions + "/" + dimensions + "_" + n + ".txt";
        if (flip.length() == 0)
            Main.main("-ext", extension, stitches, pattern);
        else
            Main.main("-ext", extension, flip, stitches, pattern);
        System.in.close();
        System.out.close();
    }

    @Before
    public void saveStreams()
    {
        savedIn = System.in;
        savedOut = System.out;
    }

    @After
    public void restoreStreams()
    {
        System.setIn(savedIn);
        System.setOut(savedOut);
    }
}
