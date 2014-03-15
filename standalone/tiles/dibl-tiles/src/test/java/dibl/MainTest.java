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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

public class MainTest
{
    private static final String INTERLEAVED_PATTERNS = "src/main/assembly/input/PairTraversal/interleaved/";
    private static final String DIAMOND_PATTERNS = "src/main/assembly/input/PairTraversal/diamond/";
    private static final String BRICK_PATTERNS = "src/main/assembly/input/PairTraversal/brick/";
    private static final String STITCHES = "4;4\n" + "tcptc;tc;tcptc;tc\n" + "tc;tcptc;tc;tcptc\n" + "tcptc;tc;tcptc;tc\n" + "tc;tcptc;tc;tcptc\n";
    private final List<Closeable> closeables = new ArrayList<Closeable>();
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
        Main.main("", "", "");
    }

    @Test
    public void flipWithoutTuples() throws Exception
    {
        Main.main("-x", "");
    }

    @Test
    public void mixedFlips() throws Exception
    {
        Main.main("-x", "-Y", "");
        Main.main("-X", "-V", "");
        Main.main("-H", "-x", "");
    }

    @Test
    public void inlineSingleArg() throws Exception
    {
        setStreams(DIAMOND_PATTERNS + "3x3.svg", "target/main1.svg");
        Main.main("-ext", "xyz", STITCHES);
    }

    @Test
    public void inlineTwoArgs() throws Exception
    {
        setStreams(DIAMOND_PATTERNS + "3x3.svg", "target/main2.svg");
        Main.main(STITCHES, "3\t3\n" //
                + "(0,1,1,0,-1,-1)\t(1,0,1,0,-1,-1)\t(0,0,1,1,-1,-1)\n" //
                + "(0,1,1,-1,-1,0)\t(-1,1,0,1,0,-1)\t(0,1,1,0,-1,-1)\n" //
                + "(-1,1,1,0,-1,0)\t(0,0,0,0,0,0)\t(1,1,0,-1,0,-1)");
    }

    @Test
    public void hybrid() throws Exception
    {
        setStreams(DIAMOND_PATTERNS + "4x4.svg", "target/mainRotated.png");
        Main.main("-x", "-y", "-ext", "png", STITCHES, DIAMOND_PATTERNS + "4x4/4x4_2.txt");
    }

    @Test
    public void hybridDiamondX() throws Exception
    {
        setStreams(DIAMOND_PATTERNS + "3x3.svg", "target/mainDiamondFlippedAlongX.jpg");
        Main.main("-x", "-ext", "jpg", STITCHES, DIAMOND_PATTERNS + "3x3/3x3_3.txt");
    }

    @Test
    public void hybridDiamondY() throws Exception
    {

        setStreams(DIAMOND_PATTERNS + "3x3.svg", "target/mainDiamondFlippedAlongY.tiff");
        Main.main("-y", "-ext", "tiff", STITCHES, DIAMOND_PATTERNS + "3x3/3x3_4.txt");
    }

    @Test
    public void rotateBrickInterleaved() throws Exception
    {

        setStreams(INTERLEAVED_PATTERNS + "2x4-pair.svg", "target/rotate.png");
        Main.main("-H", "-V", "-ext", "png", STITCHES, INTERLEAVED_PATTERNS + "2x4/2x4_4.txt");
    }

    @Test
    public void flipBrickDiagonal() throws Exception
    {
        final String template = BRICK_PATTERNS + "4x4-pair.svg";
        final String pattern = BRICK_PATTERNS + "4x4/4x4_";
        for (int i = 1; i <= 4; i++)
        {
            setStreams(template, "target/4x4_" + i + "cc.png");
            Main.main("-ext", "png", STITCHES, pattern + i + ".txt");
        }
        setStreams(template, "target/4x4_4ccH.png");
        Main.main("-X", "-ext", "png", STITCHES, pattern + "4.txt");

        setStreams(template, "target/4x4_4ccV.png");
        Main.main("-Y", "-ext", "png", STITCHES, pattern + "4.txt");

        setStreams(template, "target/4x4_4ccVH.png");
        Main.main("-X", "-Y", "-ext", "png", STITCHES, pattern + "4.txt");
    }

    @Test
    public void interleaved() throws Exception
    {
        final String targetFolder = "target/" + new File(INTERLEAVED_PATTERNS).getName() + "/";
        new File(targetFolder).mkdirs();
        for (final String dimensions : new String[] {"2x4", "4x2", "2x2"})
        {
            final String template = INTERLEAVED_PATTERNS + dimensions + "-pair.svg";
            for (int i = 1; i < 8; i++)
            {
                final String pattern = INTERLEAVED_PATTERNS + dimensions + "/" + dimensions + "_" + i + ".txt";
                final String out = targetFolder + dimensions + "_" + i;

                setStreams(template, out + ".png");
                Main.main("-ext", "png", STITCHES, pattern);

                setStreams(template, out + "-V.jpg");
                Main.main("-ext", "jpg", "-V", STITCHES, pattern);

                setStreams(template, out + "-H.jpg");
                Main.main("-ext", "jpg", "-H", STITCHES, pattern);

                setStreams(template, out + "-V.jpg");
                Main.main("-ext", "jpg", "-V", STITCHES, pattern);

                resetStreams();
            }
        }
    }

    @Test
    public void brick() throws Exception
    {
        final String targetFolder = "target/" + new File(BRICK_PATTERNS).getName() + "/";
        new File(targetFolder).mkdirs();
        for (final String dimensions : new String[] {"3x3", "4x4"})
        {
            final String template = BRICK_PATTERNS + dimensions + ".svg";
            for (int i = 1; i < 8; i++)
            {
                final String pattern = BRICK_PATTERNS + dimensions + "/" + dimensions + "_" + i + ".txt";
                final String out = targetFolder + dimensions + "_" + i;

                setStreams(template, out + ".png");
                Main.main("-ext", "png", STITCHES, pattern);

                setStreams(template, out + "-X.jpg");
                Main.main("-ext", "jpg", "-X", STITCHES, pattern);

                setStreams(template, out + "-Y.jpg");
                Main.main("-ext", "jpg", "-Y", STITCHES, pattern);

                resetStreams();
            }
        }
    }

    public void setStreams(final String in, final String string) throws Exception
    {
        final FileInputStream inputStream = new FileInputStream(in);
        final PrintStream printStream = new PrintStream(new FileOutputStream(string));
        savedIn = System.in;
        savedOut = System.out;
        System.setIn(inputStream);
        System.setOut(printStream);
        closeables.add(inputStream);
        closeables.add(printStream);
    }

    @After
    public void resetStreams() throws Exception
    {
        for (final Closeable closable : closeables)
            closable.close();
        if (savedIn != null)
            System.setIn(savedIn);
        if (savedOut != null)
            System.setOut(savedOut);
    }
}
