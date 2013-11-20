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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Matrix<H extends Flipper<String>>
{
    /** Immutable content: no method should return the field nor any of its rows. */
    private final String[][] matrix;
    private final int rows;
    private final int cols;
    private H helper;

    /** throws ArrayIndexOutOfBounds if any row is shorter than the first */
    public Matrix(final String[][] matrix,final H helper)
    {
        this.helper = helper;
        rows = matrix.length;
        cols = matrix[0].length;
        this.matrix = new String[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                this.matrix[r][c] = matrix[r][c];
    }

    public static String[][] read(final InputStream input) throws IOException
    {
        return readMatrix(input);
    }

    private static String[][] readMatrix(final InputStream input) throws IOException
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try
        {
            final String[] dimensions = reader.readLine().split("\\s");
            final int rows = Integer.parseInt(dimensions[0].trim());
            final int cols = Integer.parseInt(dimensions[1].trim());
            final String[][] matrix = new String[rows][cols];
            for (int r = 0; r < rows; r++)
            {
                final String[] cells = reader.readLine().split("\t");
                for (int c = 0; c < cols && c < cells.length; c++)
                    matrix[r][c] = cells[c];
            }
            return matrix;
        }
        finally
        {
            reader.close();
        }
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter than the first */
    public String[][] flipLeftRight()
    {
        final String[][] ret = new String[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[r][cols - 1 - c] = helper.flipLeftRight(matrix[r][c]);
        return ret;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter than the first */
    public String[][] flipBottomUp()
    {
        final String[][] ret = new String[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[rows - 1 - r][c] = helper.flipBottomUp(matrix[r][c]);
        return ret;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter than the first */
    public String[][] flipNE2SW()
    {
        final String[][] ret = new String[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[c][r] = helper.flipNE2SW(matrix[r][c]);
        return ret;
    }

    /** throws ArrayIndexOutOfBounds if any row is shorter than the first */
    public String[][] flipNW2SE()
    {
        final String[][] ret = new String[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[cols - 1 - c][rows - 1 - r] = helper.flipNW2SE(matrix[r][c]);
        return ret;
    }

    public String[][] shift(final int row, final int col)
    {
        final String[][] ret = new String[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[r][c] = matrix[(r + row) % rows][(c + col) % cols];
        return ret;
    }

    public String[][] skewDown()
    {
        final String[][] ret = new String[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[r][c] = matrix[(r + c) % rows][c];
        return ret;
    }

    public String[][] skewUp()
    {
        final String[][] ret = new String[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[r][c] = matrix[(r + rows - c) % rows][c];
        return ret;
    }

    @Override
    public String toString()
    {
        return Arrays.deepToString(matrix);
    }
}
