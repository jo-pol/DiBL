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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import dibl.Main;

public class MainTest
{
    private static final String BRICK_PATTERNS = "src/main/assembly/input/PairTraversal/brick/";
    private static final String DIAMOND_PATTERNS = "src/main/assembly/input/PairTraversal/diamond/";
    private PrintStream savedOut;
    private InputStream savedIn;

    @Test
    public void noArgs() throws Exception
    {
        Whitebox.setInternalState(Main.class, "README", "src/main/assembly/README.txt");
        Main.main();
    }

    @Test
    public void inlineSingleArg() throws Exception
    {
        System.setIn(new FileInputStream(BRICK_PATTERNS+"3x3.svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/main1.svg")));
        Whitebox.setInternalState(Main.class, "README", "src/main/assembly/README.txt");
        Main.main("3\t3\ntcptc\ttc\ttcptc\ntc\ttcptc\ttc\ntcptc\ttc\ttcptc");
    }

    @Test
    public void inlineTwoArgs() throws Exception
    {
        System.setIn(new FileInputStream(BRICK_PATTERNS+"3x3.svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/main2.svg")));
        Whitebox.setInternalState(Main.class, "README", "src/main/assembly/README.txt");
        Main.main("3\t3\ntcptc\ttc\ttcptc\ntc\ttcptc\ttc\ntcptc\ttc\ttcptc"//
                , "3\t3\n" //
                        + "(0,1,1,0,-1,-1)\t(1,0,1,0,-1,-1)\t(0,0,1,1,-1,-1)\n" //
                        + "(0,1,1,-1,-1,0)\t(-1,1,0,1,0,-1)\t(0,1,1,0,-1,-1)\n" //
                        + "(-1,1,1,0,-1,0)\t(0,0,0,0,0,0)\t(1,1,0,-1,0,-1) ");
    }

    @Test
    public void customMix3x3new() throws Exception
    {
        System.setIn(new FileInputStream(BRICK_PATTERNS + "/3x3.svg"));
        System.setOut(new PrintStream(new FileOutputStream("target/3x3.svg")));
        Main.main(BRICK_PATTERNS + "3x3/3x3_1.txt", "target/3x3", "tc", "tcptc");
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
