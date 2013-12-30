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
