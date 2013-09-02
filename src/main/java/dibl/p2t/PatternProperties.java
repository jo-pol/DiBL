package dibl.p2t;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import dibl.matrix.Extractor;

public class PatternProperties
{
    /** Index for connection with adjacent node. */
    private final int west = 3, east = 0;

    /** Orientations for tiles. Two sides in ascii-art: \__, V, __/ */
    private final String[][] t2b, l2r, r2l;

    /** dimensions of input matrix */
    private final int rows, cols;

    private String[][]ms;

    /** Later changes to the argument do not affect the created object. */
    PatternProperties(final PairTraversalPattern ptp)
    {
        rows = ptp.getNumberOfRows();
        cols = ptp.getNumberOfColumns();

        ms = new String[2 * rows][2 * cols];
        t2b = new String[rows][cols];
        l2r = new String[rows][cols];
        r2l = new String[rows][cols];
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                // concatenate the matrix 4 times into a bigger one
                t2b[r][c] = ms[r][c] = ms[r + rows][c] = ms[r][c + cols] = ms[r + rows][c + cols] = ptp.getTuple(toAlpha(c, r));
            }
        }
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                r2l[r][c] = ms[r + cols - 1 - c][c];
                l2r[r][c] = ms[r][c + rows - 1 - r];
            }
        }
    }

    public String startPoints()
    {
        return "r2l" + Arrays.deepToString(startPoints(r2l).toArray()) //
                + " t2b" + Arrays.deepToString(startPoints(t2b).toArray()) //
                + " l2r" + Arrays.deepToString(startPoints(l2r).toArray());
    }

    public String smallest()
    {
        Set<String> result = new TreeSet<String>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                result.add(Arrays.deepToString(Extractor.shift(t2b, r, c)));
        return result.iterator().next();
    }

    public String unsignedSmallest()
    {
        Set<String> result = new TreeSet<String>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                result.add(Arrays.deepToString(Extractor.shift(t2b, r, c)).replaceAll("-", ""));
        return result.iterator().next();
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

