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
import java.io.IOException;
import java.io.InputStreamReader;

import org.jdom2.JDOMException;

public class Main
{
    private static String README = "README.txt";// not final to allow WhiteboxTest

    public static void main(final String... args) throws IOException, JDOMException
    {
        if (args.length == 1)
            Generator.generate(System.in, System.out, args[0]);
        else if (args.length > 1)
            new Generator(System.in).variations(args);
        else
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
}
