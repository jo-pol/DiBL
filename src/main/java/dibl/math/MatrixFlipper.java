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

/**
 * A Helper class to flip matrices. As long as (de)serialization is cheap and complete enough matrices of
 * strings are universal enough.<br>
 * <br>
 * A generic matrix class requires much more verbose lists.<br>
 * <br>
 * <code>m[r][c]</code><br>
 * becomes <br>
 * <code>m.get( r ).get( c )</code><br>
 * <br>
 * <code>String[][] m = new String[rows][cols];</code><br>
 * becomes <br>
 * <code>
 * List&lt;T extends List&lt;T>> m = new ArrayList&lt;? extends List&lt;T>>(rows);<br>
 *     for (int r=0;r&lt;rows;r++)<br>
 *         m.get[i] = new ArrayList&lt;? extends T>(rows);<br>
 * </code>
 * 
 * @param <H>
 */
public class MatrixFlipper<H extends XYFlipper<String>> implements XYFlipper<String[][]>
{
    private final H helper;

    public MatrixFlipper(final H helper)
    {
        this.helper = helper;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter that the first */
    public String[][] flipLeftRight(final String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        final String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[r][N - 1 - c] = helper.flipLeftRight(mat[r][c]);
        return ret;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter that the first */
    public String[][] flipBottomUp(final String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        final String[][] ret = new String[N][M];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[M - 1 - r][c] = helper.flipBottomUp(mat[r][c]);
        return ret;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter that the first */
    public String[][] flipNE2SW(final String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        final String[][] ret = new String[M][N];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[c][r] = helper.flipLeftRight(mat[r][c]);
        return ret;
        // TODO code smell. Helper should be method argument: interface with one method.
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter that the first */
    public String[][] flipNW2SE(final String[][] mat)
    {
        final int M = mat.length;
        final int N = mat[0].length;
        final String[][] ret = new String[M][N];
        for (int r = 0; r < M; r++)
            for (int c = 0; c < N; c++)
                ret[N - 1 - c][M - 1 - r] = helper.flipBottomUp(mat[r][c]);
        return ret;
    }
}
