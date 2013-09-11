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
package dibl.p2t;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.jdom2.JDOMException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import dibl.Main;

public class GeneratorTest
{
    private static final String CFG = "src/main/assembly/cfg/";
    private static final String INPUT_FOLDER = "src/main/assembly/input";

    @BeforeClass
    public static void setup()
    {
        Whitebox.setInternalState(Generator.class, "CFG", CFG);
    }

    @AfterClass
    public static void cleanup()
    {
        Whitebox.setInternalState(Generator.class, "CFG", "cfg/");
    }

    @Test
    public void variants3x3_tcptc() throws Exception
    {
        int i = 0;
        for (final PairTraversalPattern pattern : loadVariants("3x3_1.txt"))
            Generator.generate(pattern, new PrintStream(new FileOutputStream("target/3x3_" + (++i) + "_tcptc.svg")), "tcptc");
    }

    @Test
    public void standardInOut3x3_1_tc() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/3x3_1_tcp.svg"));
        Generator.generate(read("3x3_1.txt"), out, "tc");
    }

    @Test
    public void standardInOut4x4_522_tc() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/4x4_522_tcp.svg"));
        Generator.generate(read("4x4_522.txt"), out, "tc");
    }

    @Test
    public void customMix3x3new() throws Exception
    {
        Main.mainNew("src/main/assembly/input/3x3_1.txt", "target/3x3", "tc", "tcptc");
    }

    //@Ignore
    // generates 16K files
    @Test
    public void stitchPermutations() throws Exception
    {
        String folder = "target/permutations";
        new File(folder).mkdirs();
        new Generator(read("3x3_1.txt")).permutations(new File(folder), "tc", "tcptc");
    }

    @Ignore
    // do not break the build with uncommitted input
    @Test
    public void allInputFiles_tcptc() throws Exception
    {
        new File("target/all").mkdirs();
        final int[] files3x3 = {1, 2, 3, 4, 5, 6, 7};
        final int[] files4x4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28, 29, 33, 34, 35, 36, 40, 41, 42,
                43, 44, 45, 47, 48, 49, 50, 51, 52, 54, 55, 56, 58, 60, 61, 65, 67, 70, 74, 75, 79, 81, 86, 87, 88, 92, 96, 98, 101, 102, 106, 109, 112, 113,
                117, 118, 122, 125, 131, 132, 133, 134, 135, 136, 137, 138, 144, 145, 146, 147, 149, 151, 153, 158, 162, 165, 166, 167, 169, 170, 171, 172,
                174, 176, 177, 178, 179, 180, 182, 185, 186, 189, 190, 191, 194, 195, 196, 197, 198, 199, 200, 201, 205, 206, 207, 209, 210, 211, 212, 221,
                222, 227, 231, 232, 242, 244, 245, 251, 254, 256, 265, 281, 291, 325, 335, 336, 338, 342, 343, 344, 345, 346, 349, 350, 351, 352, 353, 355,
                356, 360, 364, 365, 366, 367, 372, 374, 382, 390, 392, 393, 397, 407, 408, 413, 414, 417, 418, 419, 420, 421, 423, 426, 427, 428, 429, 434,
                440, 444, 446, 447, 448, 456, 466, 496, 497, 499, 500, 502, 509, 510, 512, 515, 516, 517, 518, 520, 521, 522, 558, 559, 569, 575, 601, 629,
                656, 740,};

        for (final int i : files3x3)
            gen("3x3_" + i);
        for (final int i : files4x4)
            gen("4x4_" + i);
    }

    private void gen(final String file) throws FileNotFoundException, IOException, JDOMException
    {
        final PairTraversalPattern[] variations = loadVariants(file + ".txt");
        for (int i = 0; i < variations.length; i++)
        {
            final PrintStream out = new PrintStream(new FileOutputStream("target/all/" + file + "_" + i + ".svg"));
            Generator.generate(read(file + ".txt"), out, "tcptc");
        }
        System.out.print("+");
    }

    private PairTraversalPattern[] loadVariants(final String string) throws IOException, FileNotFoundException
    {
        return new PatternProperties(read(string)).toPatterns();
    }

    private PairTraversalPattern read(final String string) throws IOException, FileNotFoundException
    {
        return new PairTraversalPattern(new FileInputStream(INPUT_FOLDER + "/" + string));
    }
}
