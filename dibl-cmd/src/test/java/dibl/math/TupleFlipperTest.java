// @formatter:off
/*
 * Copyright 2013, J. Pol
 *
 * This file is part of free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation.
 *
 * This package is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details. A copy of the GNU General Public License is
 * available at <http://www.gnu.org/licenses/>.
 */
// @formatter:on
package dibl.math;

import java.util.Arrays;

import org.junit.Test;

import dibl.diagrams.DPTP;

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
        System.out.println(Arrays.deepToString(new DPTP(pattern).flipBottomUp()));
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
