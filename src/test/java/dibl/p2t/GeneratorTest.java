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
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import dibl.p2t.Generator;

public class GeneratorTest
{
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
        final FileInputStream in = new FileInputStream("src/main/assembly/input/3x3_1.txt");

        Generator.generate(new PairTraversalPattern(in), out, "tcptc");
    }

    @Test
    public void standardInOut4x4_522_tcptc() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/4x4_522_tcptc.svg"));
        final FileInputStream in = new FileInputStream("src/main/assembly/input/4x4_522.txt");

        Generator.generate(new PairTraversalPattern(in), out, "tcptc");
    }

    @Test
    public void standardInOut3x3_1_tcp() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/3x3_1_tcp.svg"));
        final FileInputStream in = new FileInputStream("src/main/assembly/input/3x3_1.txt");

        Generator.generate(new PairTraversalPattern(in), out, "tcp");
    }

    @Test
    public void standardInOut4x4_522_tcp() throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream("target/4x4_522_tcp.svg"));
        final FileInputStream in = new FileInputStream("src/main/assembly/input/4x4_522.txt");

        Generator.generate(new PairTraversalPattern(in), out, "tcp");
    }

    @Ignore// generates 16K files
    @Test
    public void standardInOut4x4() throws Exception
    {
        final FileInputStream in = new FileInputStream("src/main/assembly/input/4x4_522.txt");

        new Generator(new PairTraversalPattern(in)).variations("tcp","tcptcp");
    }

    @Ignore// creates thousands of files once cfg/4x4.svg is developed
    @Test
    public void standardInOutAll() throws Exception
    {
        int i = 0;
        for (final File file : new File("src/test/resources/alles").listFiles())
        {
            try
            {
                // TODO rather a file or URL than a stream as output
                // not finding a proper template does not prevent creting the file
                final FileInputStream in = new FileInputStream(file);
                final PrintStream out = new PrintStream(new FileOutputStream("target/" + file.getName() + ".svg"));
                Generator.generate(new PairTraversalPattern(in), out, "tcptc");
                if (++i % 20 == 0)
                    System.out.print(" " + i);
            }
            catch (final FileNotFoundException e)
            {
            }
        }
    }
}
