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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import dibl.diagrams.PairTraversalPattern;

public class PairTraversalPatternTest
{
    private static final String INPUT = "src/main/assembly/input";

    @Test
    public void test() throws Exception
    {
        final String input = INPUT + "/3x3_1.txt";
        final PairTraversalPattern tp = new PairTraversalPattern(new FileInputStream(input));
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
        assertThat(reader.readLine(), is(tp.getNumberOfRows() + "\t" + tp.getNumberOfColumns()));
        assertThat(reader.readLine(), is(tp.getTuple("A1") + "\t" + tp.getTuple("B1") + "\t" + tp.getTuple("C1") + "\t"));
        assertThat(reader.readLine(), is(tp.getTuple("A2") + "\t" + tp.getTuple("B2") + "\t" + tp.getTuple("C2") + "\t"));
        assertThat(reader.readLine(), is(tp.getTuple("A3") + "\t" + tp.getTuple("B3") + "\t" + tp.getTuple("C3") + "\t"));
        reader.close();
    }

    @Test
    public void collectAllTuples() throws Exception
    {
        final Set<String> tuples = new TreeSet<String>();
        for (final File file : new File(INPUT).listFiles())
            if (file.getName().endsWith(".txt"))
            {

                final PairTraversalPattern t = new PairTraversalPattern(new FileInputStream(file));
                for (final String cell : t.getCellKeys())
                    tuples.add(t.getTuple(cell));
            }
        for (final Object tuple : tuples.toArray())

            System.out.println(tuple);
    }
}
