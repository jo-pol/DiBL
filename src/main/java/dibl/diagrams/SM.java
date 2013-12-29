package dibl.diagrams;

import dibl.math.Matrix;
import dibl.math.StitchFlipper;

/** Matrix implementation for Stitches, the elements need no flipping. */
public class SM extends Matrix<StitchFlipper>
{
    private static final StitchFlipper FLIPPER = new StitchFlipper();

    /**
     * Creates a Stitch Matrix. Convenience constructor for
     * {@link Matrix#Matrix(String[][], dibl.math.Flipper).
     */
    public SM(String[][] input)
    {
        super(input, FLIPPER);
    }
}
