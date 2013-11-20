package dibl.math;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LongTupleFlipperTest
{
    @Test
    public void rotateX()
    {
        checkRotation("(0,1,0,1,0,-1,0,-1)", "(0,1,0,1,0,-1,0,-1)");
    }
    @Test
    public void rotate_V_()
    {
        checkRotation("(-1,1,0,1,-1,0,0,0)", "(1,0,0,0,1,-1,0,-1)");
    }

    private void checkRotation(String input, String expected)
    {
        LongTupleFlipper transformer = new LongTupleFlipper();
        String actual = transformer.flipLeftRight(transformer.flipBottomUp(input));
        assertTrue(actual+" != "+expected,actual.equals( expected));
    }

}
