package dibl.diagrams;

import java.io.IOException;
import java.io.InputStream;

import dibl.math.Matrix;
import dibl.math.StitchFlipper;

/** Matrix implementation for Stitches, the elements need no flipping. */
public class SM extends Matrix<StitchFlipper>
{
    private static final StitchFlipper FLIPPER = new StitchFlipper();

    /** Convenience constructor for {@link Matrix#Matrix(InputStream, dibl.math.Flipper)}. */
    public SM(InputStream input) throws ArrayIndexOutOfBoundsException, IOException
    {
        super(input, FLIPPER);
    }

    /** Convenience constructor for {@link Matrix#Matrix(String[][], dibl.math.Flipper). */
    public SM(String[][] input)
    {
        super(input, FLIPPER);
    }
}
