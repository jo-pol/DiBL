package dibl.matrix;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dibl.math.TupleTransformer;

public class TupleTransformerTest
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
        TupleTransformer transformer = new TupleTransformer();
        String actual = transformer.flipLeftRight(transformer.flipBotomUp(input));
        assertTrue(actual+" != "+expected,actual.equals( expected));
    }
}
