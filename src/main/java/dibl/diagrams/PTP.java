package dibl.diagrams;

import java.io.IOException;
import java.io.InputStream;

import dibl.math.LongTupleFlipper;
import dibl.math.Matrix;

/** Matrix implementation for Pair Traversal Patterns with long tuples for interleaved patterns. */
public class PTP extends Matrix<LongTupleFlipper>
{
    private static final LongTupleFlipper FLIPPER = new LongTupleFlipper();

    /**
     * Creates a Pair Traversal Pattern. Convenience constructor for
     * {@link Matrix#Matrix(InputStream, dibl.math.Flipper)}.
     */
    public PTP(InputStream input) throws ArrayIndexOutOfBoundsException, IOException
    {
        super(input, FLIPPER);
    }

    /**
     * Creates a Pair Traversal Pattern. Convenience constructor for
     * {@link Matrix#Matrix(String[][], dibl.math.Flipper).
     */
    public PTP(String[][] input)
    {
        super(input, FLIPPER);
    }

    @Override
    public String[][] flipLeftRight()
    {
        // origin super required
        // 1-2-3- 3-2-1- 3-2-1-
        // -4-5-6 -6-5-4 -5-4-6
        // 7-8-9- 9-8-7- 9-8-7-
        return shiftOddRows(super.flipLeftRight());
    }

    @Override
    public String[][] flipBottomUp()
    {
        String[][] m = super.flipBottomUp();
        if (m[0].length % 2 == 0)
            return shiftOddRows(m);
        else
            return m;
    }

    private String[][] shiftOddRows(String[][] m)
    {
        for (int r = 1; r < m.length; r += 2)
        {
            String saved = m[r][0];
            int cols = m[r].length;
            for (int c = 1; c < cols; c += 2)
            {
                m[r][c - 1] = m[r][c];
            }
            m[r][cols - 1] = saved;
        }
        return m;
    }
}
