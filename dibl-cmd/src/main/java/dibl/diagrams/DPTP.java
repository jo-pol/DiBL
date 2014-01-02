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
package dibl.diagrams;

import dibl.math.LongTupleFlipper;
import dibl.math.Matrix;

/** Matrix implementation for Pair Traversal Patterns with long tuples for interleaved patterns. */
public class DPTP extends Matrix<LongTupleFlipper>
{
    private static final LongTupleFlipper FLIPPER = new LongTupleFlipper();


    /**
     * Creates a Pair Traversal Pattern for Diamond patterns with brick tiles. Convenience constructor for
     * {@link Matrix#Matrix(String[][], dibl.math.Flipper).
     */
    public DPTP(String[][] input)
    {
        super(input, FLIPPER);
    }

    @Override
    public String[][] flipLeftRight()
    {
        // input | super | output
        // ========================
        // 1-2-3- | 3-2-1- | 3-2-1-
        // -4-5-6 | -6-5-4 | -5-4-6
        // 7-8-9- | 9-8-7- | 9-8-7-
        // ==============================
        // A-B-C-D- | D-C-B-A- | D-C-B-A-
        // -E-F-G-H | -H-G-F-E | -G-F-E-H
        // I-J-K-L- | L-K-J-I- | L-K-J-I-
        // -M-N-O-P | -P-O-N-M | -O-N-M-P
        // ==============================
        return shiftOddRows(super.flipLeftRight());
    }

    @Override
    public String[][] flipBottomUp()
    {
        // input | super | output
        // ========================
        // 1-2-3- | 7-8-9-
        // -4-5-6 | -4-5-6
        // 7-8-9- | 1-2-3-
        // ==============================
        // A-B-C-D- | M-N-O-P- | M-N-O-P-
        // -E-F-G-H | -I-J-K-L | -J-K-L-I
        // I-J-K-L- | E-F-G-H- | E-F-G-H-
        // -M-N-O-P | -A-B-C-D | -B-C-D-A
        // ==============================
        String[][] m = super.flipBottomUp();
        if (m[0].length % 2 == 0)
            return shiftOddRows(m);
        else
            return m;
    }

    private String[][] shiftOddRows(String[][] m)
    {
        for (int r = 1; r < m.length; r += 2)
        {
            String saved = m[r][0];
            int cols = m[r].length;
            for (int c = 1; c < cols; c += 2)
            {
                m[r][c - 1] = m[r][c];
            }
            m[r][cols - 1] = saved;
        }
        return m;
    }
}
