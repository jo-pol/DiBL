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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Matrix<F extends Flipper<String>>
{
    /** Immutable content: no method should return the field nor any of its rows. */
    private final String[][] matrix;
    private final int rows;
    private final int cols;
    private final F flipper;

    /**
     * Creates an instance that is not affected by later changes to the constructor argument.
     * 
     * @param matrix
     * @param flipper
     *        flips the elements of the matrix along with the matrix
     * @throws ArrayIndexOutOfBoundsException
     *         if any row is shorter than the first
     */
    public Matrix(final String[][] matrix, final F flipper) throws ArrayIndexOutOfBoundsException
    {
        this(flipper, matrix);
    }

    /** Convenience constructor for <code>new Matrix<...>(Matrix.read(input),...)</code>. */
    public Matrix(final InputStream input, final F flipper) throws ArrayIndexOutOfBoundsException, IOException
    {
        this(flipper, readMatrix(input));
    }

    private Matrix(final F flipper, final String[][] matrix)
    {
        this.flipper = flipper;
        rows = matrix.length;
        cols = matrix[0].length;
        this.matrix = new String[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                this.matrix[r][c] = matrix[r][c];
    }

    /**
     * Reads a tab separated file. The first line contains the dimensions of the matrix to read: rows,
     * columns. The subsequent lines contain the values for the matrix.
     * 
     * @param input
     * @return a matrix
     * @throws RuntimeException
     *         in case of shorter or fewer rows than specified by the dimensions
     * @throws IOException
     */
    public static String[][] read(final InputStream input) throws IOException
    {
        return readMatrix(input);
    }

    private static String[][] readMatrix(final InputStream input) throws IOException
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try
        {
            final String[] dimensions = readDimensions(reader);
            final int rows = Integer.parseInt(dimensions[0].trim());
            final int cols = Integer.parseInt(dimensions[1].trim());
            final String[][] matrix = new String[rows][cols];
            for (int r = 0; r < rows; r++)
            {
                final String[] cells = readLine(reader, r, cols);
                for (int c = 0; c < cols; c++)
                    matrix[r][c] = cells[c];
            }
            return matrix;
        }
        finally
        {
            reader.close();
        }
    }

    private static String[] readLine(final BufferedReader reader, final int lineNumber, final int cols) throws IOException
    {
        try
        {
            final String[] split = reader.readLine().split("[;\t]");
            if (split.length < cols)
                throw new IllegalArgumentException("too few elements on line " + lineNumber + " expecting " + cols + " got " + split.length);
            return split;
        }
        catch (final NullPointerException e)
        {
            throw new EOFException("at line " + lineNumber);
        }
    }

    private static String[] readDimensions(final BufferedReader reader) throws IOException
    {
        try
        {
            final String[] split = reader.readLine().split("[^0-9]+");
            if (split.length < 2)
                throw new IllegalArgumentException("expecting two numbers on the first line with the dimensions of the matrix");
            return split;
        }
        catch (final NullPointerException e)
        {
            throw new EOFException("while reading the dimensions of a matrix");
        }
    }

    public String[][] flipLeftRight()
    {
        final String[][] ret = new String[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[r][cols - 1 - c] = flipper.flipLeftRight(matrix[r][c]);
        return ret;
    }

    public String[][] flipBottomUp()
    {
        final String[][] ret = new String[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[rows - 1 - r][c] = flipper.flipBottomUp(matrix[r][c]);
        return ret;
    }

    public String[][] flipNE2SW()
    {
        final String[][] ret = new String[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[c][r] = flipper.flipNE2SW(matrix[r][c]);
        return ret;
    }

    public String[][] flipNW2SE()
    {
        final String[][] ret = new String[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ret[cols - 1 - c][rows - 1 - r] = flipper.flipNW2SE(matrix[r][c]);
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

    public boolean isShifted(final String[][] shiftedMatrix)
    {
        final String shifted = Arrays.deepToString(shiftedMatrix);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (Arrays.deepToString(shift(r, c)).equals(shifted))
                    return true;
        return false;
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
