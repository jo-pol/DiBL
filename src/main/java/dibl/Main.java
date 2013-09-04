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
package dibl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jdom2.JDOMException;

import dibl.p2t.Generator;
import dibl.p2t.PairTraversalPattern;

public class Main
{
    private static String README = "README.txt";// not final to allow WhiteboxTest

    public static void main(final String... args) throws IOException, JDOMException
    {
        if (args.length < 1)
            showUsage();
        else
        {
            final PairTraversalPattern pattern = new PairTraversalPattern(System.in);
            if (args.length == 1)
                Generator.generate(pattern, System.out, args[0]);
            else
                new Generator(pattern).variations(args);
        }
    }

    private static void showUsage() throws FileNotFoundException, IOException
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(README)));
        try
        {
            String line;
            while (null != (line = reader.readLine()))
                System.out.println(line);
        }
        finally
        {
            reader.close();
        }
    }
}
