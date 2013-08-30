package dibl;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class PatternProperties
{
    /** Index for connection with adjacent node. */
    private final int west = 3, east = 0;

    /** Orientations for tiles. Two sides in ascii-art: \__, V, __/ */
    private final int[][][] t2b, l2r, r2l;

    /** dimensions of input matrix */
    private final int rows, cols;

    private int[][][] ms;

    /** Later changes to the argument do not affect the created object. */
    PatternProperties(final PairTraversalPattern ptp)
    {
        rows = ptp.getNumberOfRows();
        cols = ptp.getNumberOfColumns();

        ms = new int[2 * rows][2 * cols][6];
        t2b = new int[2 * rows][2 * cols][6];
        l2r = new int[2 * rows][2 * cols][6];
        r2l = new int[2 * rows][2 * cols][6];
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                final String[] tuple = ptp.getTuple(toAlpha(c, r)).replaceAll("[()]", "").split(",");
                tuple[0] = tuple[0].replace("(", "");
                tuple[tuple.length - 1] = tuple[tuple.length - 1].replace(")", "");
                final int[] connections = new int[tuple.length];
                for (int i = 0; i < tuple.length && i < 6; i++)
                    connections[i] = Integer.parseInt(tuple[i]);
                // concatenate the matrix 4 times into a bigger one
                t2b[r][c] = ms[r][c] = ms[r + rows][c] = ms[r][c + cols] = ms[r + rows][c + cols] = connections;
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
        return "r2l" + Arrays.toString(startPoints(r2l).toArray()) //
                + " t2b" + Arrays.toString(startPoints(t2b).toArray()) //
                + " l2r" + Arrays.toString(startPoints(l2r).toArray());
    }

    public String smallest()
    {
        Set<String> result = new TreeSet<String>();
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < cols; x++)
            {
                String s = "";
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++)
                        s += Arrays.toString(ms[r + y][c + x]);
                result.add(s);
            }
        return result.iterator().next();
    }

    public String unsignedSmallest()
    {
        Set<String> result = new TreeSet<String>();
        for (int y = 0; y < rows; y++)
            for (int x = 0; x < cols; x++)
            {
                String s = "";
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++)
                        s += Arrays.toString(ms[r + y][c + x]).replaceAll("-", "");
                result.add(s);
            }
        return result.iterator().next();
    }

    private Set<String> startPoints(final int[][][] m)
    {
        final Set<String> result = new TreeSet<String>();
        final Set<Integer> properColumns = new TreeSet<Integer>();
        final Set<Integer> properRows = new TreeSet<Integer>();
        for (int r = 0; r < rows; r++)
        {
            int properTuples = 0;
            for (int c = 0; c < cols; c++)
            {
                if (0 <= m[r][c][east])
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
                if (0 <= m[r][c][west])
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
