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
package dibl.math;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import dibl.math.Flipper;
import dibl.math.Matrix;

public class MatrixTest
{
    private static TF transformer=new TF();
    private static String[][] m = { {"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};
    private static final Matrix<TF> M = new Matrix<TF>(m,transformer);
    private static String[][] m4 = { {"a", "b", "c", "d"}, {"e", "f", "g", "h"}, {"i", "j", "k", "l"}, {"m", "n", "o", "p"}};
    private static final Matrix<TF> M4 = new Matrix<TF>(m4,transformer);
    private static class TF implements Flipper<String>{
        @Override
        public String flipLeftRight(final String o)
        {
            return o;
        }

        @Override
        public String flipBottomUp(final String o)
        {
            return o;
        }

        @Override
        public String flipNW2SE(String o)
        {
            return o;
        }

        @Override
        public String flipNE2SW(String o)
        {
            return o;
        }
    }
    @Test
    public void skewDown()
    {
        final String s = Arrays.deepToString(M.skewDown());
        assertThat(s, is("[[1, 5, 9], [4, 8, 3], [7, 2, 6]]"));
    }

    @Test
    public void skewUp()
    {
        final String s = Arrays.deepToString(M.skewUp());
        assertThat(s, is("[[1, 8, 6], [4, 2, 9], [7, 5, 3]]"));
    }

    @Test
    public void shift()
    {
        final String s = Arrays.deepToString(M.shift( 1, 1));
        assertThat(s, is("[[5, 6, 4], [8, 9, 7], [2, 3, 1]]"));
    }

    @Test
    public void ne2sw()
    {
        final String s = Arrays.deepToString(M4.flipNE2SW());
        assertThat(s, is("[[a, e, i, m], [b, f, j, n], [c, g, k, o], [d, h, l, p]]"));
    }

    @Test
    public void nw2se()
    {
        final String s = Arrays.deepToString(M4.flipNW2SE());
        assertThat(s, is("[[p, l, h, d], [o, k, g, c], [n, j, f, b], [m, i, e, a]]"));
    }

    @Test
    public void flipBottomUp()
    {
        final String s = Arrays.deepToString(M.flipBottomUp());
        assertThat(s, is("[[7, 8, 9], [4, 5, 6], [1, 2, 3]]"));
    }

    @Test
    public void flipLeftRight()
    {
        final String s = Arrays.deepToString(M.flipLeftRight());
        assertThat(s, is("[[3, 2, 1], [6, 5, 4], [9, 8, 7]]"));
    }

    @Test
    public void rotate180()
    {
        final String s = Arrays.deepToString(new Matrix<TF>(M.flipBottomUp(),transformer).flipLeftRight());
        assertThat(s, is("[[9, 8, 7], [6, 5, 4], [3, 2, 1]]"));
    }
}
