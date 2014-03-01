package dibl.diagrams;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class DPTPTest
{
    private static String[][] m = { {"1,1,1,1,1,1", "2,2,2,2,2,2", "3,3,3,3,3,3"}, {"4,4,4,4,4,4", "5,5,5,5,5,5", "6,6,6,6,6,6"}, {"7,7,7,7,7,7", "8,8,8,8,8,8", "9,9,9,9,9,9"}};

    @Test
    public void x()
    {
        final String s = Arrays.deepToString(new DPTP(m).flipLeftRight());
        assertThat(s, is("[[(3,3,0,3,3,3,0,3), (2,2,0,2,2,2,0,2), (1,1,0,1,1,1,0,1)], [(5,5,0,5,5,5,0,5), (4,4,0,4,4,4,0,4), (6,6,0,6,6,6,0,6)], [(9,9,0,9,9,9,0,9), (8,8,0,8,8,8,0,8), (7,7,0,7,7,7,0,7)]]"));
    }
}
