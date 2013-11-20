package dibl.diagrams;

import java.io.IOException;
import java.io.InputStream;

import dibl.math.LongTupleFlipper;
import dibl.math.Matrix;

/** Matrix implementation for Pair Traversal Patterns with long tuples for interleaved patterns. */
public class PTP extends Matrix<LongTupleFlipper>
{
    private static final LongTupleFlipper FLIPPER = new LongTupleFlipper();

    /** Convenience constructor for {@link Matrix#Matrix(InputStream, dibl.math.Flipper)}. */
    public PTP(InputStream input) throws ArrayIndexOutOfBoundsException, IOException
    {
        super(input, FLIPPER);
    }

    /** Convenience constructor for {@link Matrix#Matrix(String[][], dibl.math.Flipper). */
    public PTP(String[][] input)
    {
        super(input, FLIPPER);
    }
}
