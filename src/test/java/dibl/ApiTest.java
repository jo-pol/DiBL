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
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import dibl.diagrams.PTP;
import dibl.diagrams.SM;
import dibl.diagrams.Template;
import dibl.math.Matrix;
import dibl.math.ShortTupleFlipper;

public class ApiTest
{
    private static final String DIAMOND_FOLDER = "src/main/assembly/input/PairTraversal/diamond/";
    private static final String BRICK_FOLDER = "src/main/assembly/input/PairTraversal/brick/";
    private static final String OUTPUT_FOLDER = "target/" + ApiTest.class.getSimpleName() + "/";
    private final List<Closeable> closeables = new ArrayList<Closeable>();
    private final String[][] stitches = new String[][] { //
    {"tcptc", "tc", "tcptc", "tc"}, //
            {"tc", "tcptc", "tc", "tcptc"}, //
            {"tcptc", "tc", "tcptc", "tc"}, //
            {"tc", "tcptc", "tc", "tcptc"}};

    @BeforeClass
    public static void createFolder()
    {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @After
    public void clearClosables() throws IOException
    {
        for (final Closeable closeable : closeables)
            closeable.close();
        closeables.clear();
    }

    @Test
    public void all() throws Exception
    {
        loop("3x3", 8, new Template(BRICK_FOLDER + "3x3.svg"));
        loop("4x4", 222, new Template(BRICK_FOLDER + "4x4.svg"));
    }

    private void loop(final String dimensions, final int n, final Template template) throws Exception
    {
        final PrintStream out = new PrintStream(new FileOutputStream(OUTPUT_FOLDER + dimensions + "flip.txt"));
        for (int i = 1; i < n; i++)
        {
            final String[][] tuples = Matrix.read(openInput(BRICK_FOLDER + dimensions + "/" + dimensions +"_" + i + ".txt"));
            template.replaceBoth(stitches, tuples).write(openOutput(OUTPUT_FOLDER + dimensions + i + ".svg"));
            out.println((dimensions + i));
            out.println("  " + Arrays.deepToString(tuples));
            out.println("X " + Arrays.deepToString(new PTP(tuples).flipBottomUp()));
            out.println("Y " + Arrays.deepToString(new PTP(tuples).flipLeftRight()));
        }
        out.close();
    }

    @Test
    public void flipDiamondAlongX() throws Exception
    {
        final String[][] input = Matrix.read(openInput(DIAMOND_FOLDER + "4x4/4x4_1.txt"));
        final String[][] flippedTuples = new Matrix<ShortTupleFlipper>(input, new ShortTupleFlipper()).flipNW2SE();
        final Template template = new Template(openInput(DIAMOND_FOLDER + "4x4.svg"));
        template.replaceBoth(new SM(stitches).flipNW2SE(), flippedTuples);
        template.write(openOutput(OUTPUT_FOLDER + "4x4_1_flippedDiamondAlongX.png"));
    }

    @Test
    public void flipDiamondAlongY() throws Exception
    {
        final String[][] input = Matrix.read(openInput(DIAMOND_FOLDER + "4x4/4x4_1.txt"));
        final String[][] flippedTuples = new Matrix<ShortTupleFlipper>(input, new ShortTupleFlipper()).flipNE2SW();
        final Template template = new Template(openInput(DIAMOND_FOLDER + "4x4.svg"));
        template.replaceBoth(new SM(stitches).flipNE2SW(), flippedTuples);
        template.write(openOutput(OUTPUT_FOLDER + "4x4_1_flippedOldAlongY.svg"));
    }

    @Test
    public void colorCoded() throws Exception
    {
        final String[][] stitches = new String[][] { //
        {"-tc", "-ctc", "-tctc"}, //
                {"-tctc", "-tc", "-ctc"}, //
                {"-tcptc", "-ctcpctc", "-tctcptctc",}};
        final String[][] tuples = PTP.read(openInput(BRICK_FOLDER + "3x3/3x3_1.txt"));
        final Template template = new Template(openInput(BRICK_FOLDER + "3x3.svg"));
        template.replaceBoth(stitches, tuples);
        template.write(openOutput(OUTPUT_FOLDER + "3x3_1ccNormal.svg"));
        template.replaceBoth(//
                new SM(stitches).flipBottomUp(),//
                new PTP(tuples).flipBottomUp());
        template.write(openOutput(OUTPUT_FOLDER + "3x3_1ccBottomUp.svg"));
        template.replaceBoth(//
                new SM(stitches).flipLeftRight(), //
                new PTP(tuples).flipLeftRight());
        template.write(openOutput(OUTPUT_FOLDER + "3x3_1ccLeftRight.svg"));
        template.replaceBoth(//
                new SM(new SM(stitches).flipBottomUp()).flipLeftRight(),//
                new PTP(new PTP(tuples).flipBottomUp()).flipLeftRight());
        template.write(openOutput(OUTPUT_FOLDER + "3x3_1ccRotated.svg"));
    }

    private FileInputStream openInput(final String file) throws Exception
    {
        final FileInputStream inputStream = new FileInputStream(file);
        closeables.add(inputStream);
        return inputStream;
    }

    private FileOutputStream openOutput(final String file) throws Exception
    {
        final FileOutputStream outputStream = new FileOutputStream(file);
        closeables.add(outputStream);
        return outputStream;
    }
}
