package dibl.math;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

import dibl.diagrams.PTP;

public class TupleFlipperTest
{
    String[] tuples = new String[] {"(-1,0,1,1,-1,0)", "(-1,0,1,1,0,-1)", "(-1,1,0,1,-1,0)", "(-1,1,0,1,0,-1)", "(-1,1,1,-1,0,0)", "(-1,1,1,0,-1,0)",
            "(-1,1,1,0,0,-1)", "(0,0,0,0,0,0)", "(0,0,1,1,-1,-1)", "(0,1,0,1,-1,-1)", "(0,1,1,-1,-1,0)", "(0,1,1,-1,0,-1)", "(0,1,1,0,-1,-1)",
            "(1,0,0,1,-1,-1)", "(1,0,1,-1,-1,0)", "(1,0,1,-1,0,-1)", "(1,0,1,0,-1,-1)", "(1,1,0,-1,-1,0)", "(1,1,0,-1,0,-1)", "(1,1,0,0,-1,-1)",};
    String[][] pattern = new String[][] { {"(0,1,1,0,-1,-1)", "(1,0,1,0,-1,-1)", "(0,0,1,1,-1,-1)"},//
            {"(0,1,1,-1,-1,0)", "(-1,1,0,1,0,-1)", "(0,1,1,0,-1,-1)"}, //
            {"(-1,1,1,0,-1,0)", "(0,0,0,0,0,0)", "(1,1,0,-1,0,-1)"}};
    LongTupleFlipper longFlipper = new LongTupleFlipper();
    ShortTupleFlipper shortFlipper = new ShortTupleFlipper();

    @Test
    public void compareMatrix()
    {
        System.out.println(Arrays.deepToString(new PTP(pattern).flipBottomUp()));
        System.out.println(Arrays.deepToString(new Matrix<ShortTupleFlipper>(pattern,new ShortTupleFlipper()).flipBottomUp()));
    }
    @Test
    public void compareTuples()
    {
        for (String tuple : tuples)
        {
            System.out.println(longFlipper.flipBottomUp(tuple) + " " + longFlipper.flipLeftRight(tuple));
            System.out.println(shortFlipper.flipBottomUp(tuple) + "     " + shortFlipper.flipLeftRight(tuple));
            // assertThat(longFlipper.flipBottomUp(tuple),is(shortFlipper.flipBottomUp(tuple)));
            // assertThat(longFlipper.flipLeftRight(tuple),is(shortFlipper.flipLeftRight(tuple)));
        }
    }
}
