package dibl.diagrams;

import dibl.math.LongTupleFlipper;
import dibl.math.Matrix;

public class IPTP  extends Matrix<LongTupleFlipper>
{
    private static final LongTupleFlipper FLIPPER = new LongTupleFlipper();

    /**
     * Creates a Pair Traversal Pattern for Interleaved patterns with Checker board tiles. Convenience constructor for
     * {@link Matrix#Matrix(String[][], dibl.math.Flipper).
     */
    public IPTP(String[][] input)
    {
        super(input,FLIPPER);
    }
}
