// @formatter:off
/*
 * Copyright 2014, J. Pol
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
package dibl.tools;

import java.util.ArrayList;
import java.util.List;

public class Tuple
{
    final String tuple;
    private final List<Integer> terms = new ArrayList<Integer>();
    private static List<Tuple> list;

    public Tuple(final String tuple)
    {
        this.tuple = tuple;

        for (final String i : tuple.replaceAll("[()]", "").trim().split(","))
            terms.add(Integer.parseInt(i.trim()));
        terms.add(terms.get(0));

        if (nrOfZeros() != 4 || firstIn() == -1 || firstOut() == -1 || lastIn() == -1 || lastOut() == -1)
            throw new IllegalArgumentException(tuple);
    }

    private int nrOfZeros()
    {
        int count = 0;
        for (int i = 0; i < 8; i++)
            if (terms.get(i) == 0)
                count++;
        return count;
    }

    public String toString()
    {
        return tuple;
    }

    int firstIn()
    {
        for (int i = 0; i < 5; i++)
            if (terms.get(i) > 0)
                return i;
        return -1;
    }

    int lastIn()
    {
        for (int i = firstIn() + 1; i < 5; i++)
            if (terms.get(i) > 0)
                return i;
        return -1;
    }

    int firstOut()
    {
        for (int i = 4; i < 9; i++)
            if (terms.get(i) < 0)
                return i;
        return -1;
    }

    int lastOut()
    {
        for (int i = firstOut() + 1; i < 9; i++)
            if (terms.get(i) < 0)
                return i % 8;
        return -1;
    }

    public static List<Tuple> list()
    {
        if (list == null)
        {
            list = new ArrayList<Tuple>();
            final int max = Integer.parseInt("22222222", 3);
            for (int i = 0; i <= max; i++)
            {
                try
                {
                    final String digits = Integer.toString(i, 3);
                    final String padded = ("00000000" + digits).substring(digits.length());
                    final String s = padded.replaceAll("", ",").replaceAll("0", "-1").replaceAll(",1", ",0").replaceAll("2", "1").replaceAll("^,", "(")
                            .replaceAll(",$", ")");
                    list.add(new Tuple(s));
                }
                catch (final IllegalArgumentException e)
                {
                    continue;
                }
            }
        }
        return list;
    }
}
