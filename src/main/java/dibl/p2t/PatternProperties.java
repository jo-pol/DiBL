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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import dibl.math.Extractor;
import dibl.math.MatrixTransformer;

@Deprecated
public class PatternProperties
{
    /** Index for connection with adjacent node. */
    private final int west = 3, east = 0;

    private final String[][] matrix;

    /** dimensions of input matrix */
    private final int rows, cols;

    private static MatrixTransformer<TupleTransformer> matrixTransformer;

    /** Later changes to the argument do not affect the created object. */
    PatternProperties(final PairTraversalPattern ptp)
    {
        rows = ptp.getNumberOfRows();
        cols = ptp.getNumberOfColumns();
        matrixTransformer = new MatrixTransformer<TupleTransformer>(new TupleTransformer());

        matrix = new String[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                matrix[r][c] = ptp.getTuple(toAlpha(c, r));
    }

    public PairTraversalPattern[] toPatterns(){
        // TODO prevent duplicates, sadly 2D arrays lack comparators so we can't put them in a set
        List<String[][]> matrixes = new ArrayList<String[][]>();
        matrixes.add(matrix);
        matrixes.add(matrixTransformer.flipNE2SW(matrix));
        matrixes.add(matrixTransformer.flipNW2SE(matrix));
        matrixes.add(matrixTransformer.rotate180(matrix));
        PairTraversalPattern[] result =  new PairTraversalPattern[matrixes.size()];
        for (int i=0;i<result.length;i++)
            result[i] = new PairTraversalPattern((String[][])matrixes.toArray()[i]);
        return result;
    }

    public PairTraversalPattern toPattern(){
        return new PairTraversalPattern(matrix);
    }

    public PairTraversalPattern toBottomUpPattern(){
        return new PairTraversalPattern(matrixTransformer.flipNW2SE(matrix));
    }

    public PairTraversalPattern toLeftRightPattern(){
        return new PairTraversalPattern(matrixTransformer.flipNE2SW(matrix));
    }

    public PairTraversalPattern toRotatedPattern(){
        return new PairTraversalPattern(matrixTransformer.rotate180(matrix));
    }
    
    public String startPoints()
    {
        return Arrays.deepToString(startPoints(matrix).toArray());
    }

    public String smallest()
    {
        Set<String> result = new TreeSet<String>();
        addSigned(result, matrix);
        return result.iterator().next();
    }

    private void addSigned(Set<String> result, String[][] matrix2)
    {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                result.add(Arrays.deepToString(Extractor.shift(matrix2, r, c)));
    }

    public String unsignedSmallest()
    {
        Set<String> result = new TreeSet<String>();
        addUnsigned(result, matrix);
        addUnsigned(result, matrixTransformer.flipNE2SW(matrix));
        addUnsigned(result, matrixTransformer.flipNW2SE(matrix));
        addUnsigned(result, matrixTransformer.rotate180(matrix));
        return result.iterator().next();
    }

    private void addUnsigned(Set<String> result, String[][] matrix2)
    {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                result.add(Arrays.deepToString(Extractor.shift(matrix2, r, c)).replaceAll("-", ""));
    }

    private Set<String> startPoints(final String[][] m)
    {
        final Set<String> result = new TreeSet<String>();
        final Set<Integer> properColumns = new TreeSet<Integer>();
        final Set<Integer> properRows = new TreeSet<Integer>();
        for (int r = 0; r < rows; r++)
        {
            int properTuples = 0;
            for (int c = 0; c < cols; c++)
            {
                if (!m[r][c].split(",")[east].contains("-"))
                    properTuples++;
            }
            if (properTuples == cols)
                properRows.add(r);
        }
        for (int c = 0; c < cols; c++)
        {
            int properTuples = 0;
            for (int r = 0; r < rows; r++)
            {
                if (!m[r][c].split(",")[west].contains("-"))
                    properTuples++;
            }
            if (properTuples == cols)
                properColumns.add(c);
        }
        for (int r = 0; r < rows; r++)
            if (properRows.contains(r))
                for (int c = 0; c < cols; c++)
                    if (properColumns.contains(c))
                        result.add(toAlpha(r, c));
        return result;
    }

    private String toAlpha(final int column, final int row)
    {
        return "ABCDEFGHIJ".substring(column, column + 1) + (row + 1);
    }
}
