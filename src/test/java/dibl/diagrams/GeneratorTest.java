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
package dibl.diagrams;

import java.io.File;
import java.io.FileInputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
    public void customMix3x3new() throws Exception
    {
        Main.main("src/main/assembly/input/3x3_1.txt", "target/3x3", "tc", "tcptc");
    }

    @Test
    public void stitchPermutations() throws Exception
    {
        // generates 256K files
        final Template template = new Template(new FileInputStream("src/main/assembly/cfg/3x3.svg"));
        final String[] stitchTypes = {"tc", "tcptc"};
        final File folder = new File("target/permutations");
        folder.mkdirs();
        Generator.permutations(template, folder, stitchTypes);
    }

    @Test
    public void flanders() throws Exception
    {
        final File folder = new File("target/flanders");
        folder.mkdirs();
        final Template template = new Template(new FileInputStream(INPUT_FOLDER + "/flanders.svg"));
        Generator.permutations(template, folder, "tc", "ctc", "tctc");
    }
}
