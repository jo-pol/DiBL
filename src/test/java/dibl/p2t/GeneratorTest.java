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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class GeneratorTest
{
    private static final String INPUT_FOLDER = "src/main/assembly/input";

    @BeforeClass
    public static void setup()
    {
        Whitebox.setInternalState(Generator.class, "CFG", "src/main/assembly/cfg/");
    }

    @AfterClass
    public static void cleanup()
    {
        Whitebox.setInternalState(Generator.class, "CFG", "cfg/");
    }

    @Test
    public void standardInOut3x3_1_tcptc() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/3x3_1_tcptc.svg"));
        Generator.generate(read("3x3_1.txt"), out, "tcptc");
    }

    @Test
    public void standardInOut4x4_522_tcptc() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/4x4_522_tcptc.svg"));
        Generator.generate(read("4x4_522.txt"), out, "tcptc");
    }

    @Test
    public void standardInOut3x3_1_tc() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/3x3_1_tcp.svg"));
        Generator.generate(read("3x3_1.txt"), out, "tcp");
    }

    @Test
    public void standardInOut4x4_522_tc() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/4x4_522_tcp.svg"));
        Generator.generate(read("4x4_522.txt"), out, "tcp");
    }

    @Test
    public void customMix4x4() throws Exception
    {
        final String[][] stiches = { {"tcptc", "tc", "tcptc", "tc"}, {"tc", "tcptc", "tc", "tcptc"}, {"tcptc", "tc", "tcptc", "tc"},
                {"tc", "tcptc", "tc", "tcptc"}};
        final PairTraversalPattern[] variants = loadVariants("4x4_522.txt");
        Generator.generateCustomMix(stiches, "target/custom-mix4x4", variants);
    }

    @Test
    public void customMix3x3() throws Exception
    {
        final String[][] stiches = { {"tc", "tcptc", "tc"}, {"tc", "tcptc", "tc"}, {"tcptc", "tc", "tcptc"}};
        final PairTraversalPattern[] variants = loadVariants("3x3_1.txt");
        Generator.generateCustomMix(stiches, "target/custom-mix3x3", variants);
    }

    @Ignore
    // generates 16K files
    @Test
    public void stitchPermutations() throws Exception
    {
        new Generator(read("4x4_522.txt")).variations("tc", "tcptcp");
    }

    @Test
    public void allInputFiles_tcptc() throws Exception
    {
        String outputFolder = "target/all/";
        new File(outputFolder).mkdirs();
        int i = 0;
        for (final File file : new File(INPUT_FOLDER).listFiles())
        {
            final FileInputStream in = new FileInputStream(file);
            final PrintStream out = new PrintStream(new FileOutputStream(outputFolder + file.getName() + ".svg"));
            Generator.generate(new PairTraversalPattern(in), out, "tcptc");
            if (++i % 20 == 0)
                System.out.print(" " + i);
        }
    }

    private PairTraversalPattern[] loadVariants(final String string) throws IOException, FileNotFoundException
    {
        return new PatternProperties(read(string)).toVariationsArray();
    }

    private PairTraversalPattern read(final String string) throws IOException, FileNotFoundException
    {
        return new PairTraversalPattern(new FileInputStream(INPUT_FOLDER+"/" + string));
    }
}
