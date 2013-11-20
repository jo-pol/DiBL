package dibl.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dibl.math.ShortTupleFlipper;

public class ShortTupleFlipperTest
{
    @Test
    public void rotateX()
    {
        checkRotation("(0,1,1,0,-1,-1)", "(0,1,1,0,-1,-1)");
    }
    @Test
    public void rotate_V_()
    {
        checkRotation("(-1,1,1,-1,0,0)", "(1,0,0,1,-1,-1)");
    }

    private void checkRotation(String input, String expected)
    {
        ShortTupleFlipper transformer = new ShortTupleFlipper();
        String actual = transformer.flipLeftRight(transformer.flipBottomUp(input));
        assertTrue(actual+" != "+expected,actual.equals( expected));
    }
}
