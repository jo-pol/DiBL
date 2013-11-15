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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Deprecated
public class PairTraversalPattern
{
    private final Map<String, String> cells = new TreeMap<String, String>();
    private final int nrOfRows;
    private final int nrOfColumns;

    /**
     * @param input
     *        The first line contains two numbers separated with white space specifying the dimensions of
     *        the matrix. Subsequent lines contain the tuples of the matrix.
     * @throws IOException
     */
    public PairTraversalPattern(final InputStream input) throws IOException
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try
        {
            final String[] dimensions = reader.readLine().split("\\s");
            nrOfRows = Integer.parseInt(dimensions[0].trim());
            nrOfColumns = Integer.parseInt(dimensions[1].trim());
            for (int row = 0; row < nrOfRows; row++)
            {
                final String[] tuples = reader.readLine().split("\t");
                for (int column = 0; column < tuples.length; column++)
                {
                    if (tuples[column].length() > 0)
                        cells.put(toCellID(row, column), tuples[column]);
                }
            }
        }
        finally
        {
            reader.close();
        }
    }

    public PairTraversalPattern(final String[][] tuples)
    {
        nrOfRows = tuples.length;
        nrOfColumns = tuples[0].length;
        for (int row = 0; row < nrOfRows; row++)
            for (int column = 0; column < nrOfColumns; column++)
                cells.put(toCellID(row, column), tuples[row][column]);
    }

    /**
     * Gets a tuple describing connections with adjacent nodes.
     * 
     * @param cell
     *        a capital letter indicating the column, followed by a number indicating the row
     * @return the tuple at the specified row and column
     */
    public String getTuple(final String cell)
    {
        return cells.get(cell);
    }

    public String getTuple(final int row, final int column)
    {
        return cells.get(toCellID(row, column));
    }

    private String toCellID(final int row, final int column)
    {
        return "ABCDEFGHIJ".substring(column, column + 1) + (row + 1);
    }

    public Set<String> getCellKeys()
    {
        return cells.keySet();
    }

    public int getNumberOfRows()
    {
        return nrOfRows;
    }

    public int getNumberOfColumns()
    {
        return nrOfColumns;
    }

    public String getDimensions()
    {
        return nrOfRows + "x" + nrOfColumns;
    }

    public boolean isEmpty(final String cell)
    {
        return cells.get(cell).matches("[(0,)]+");
    }

    public boolean isEmpty(final int row, final int column)
    {
        return cells.get(toCellID(row, column)).matches("[(0,)]+");
    }
}
