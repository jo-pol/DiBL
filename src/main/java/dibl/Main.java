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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jdom2.JDOMException;

import dibl.diagrams.Template;
import dibl.math.Matrix;

public class Main
{
    private static String README = "README.txt";// not final to allow WhiteboxTest

    public static void main(final String... args) throws IOException, JDOMException
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        switch (args.length)
        {
        case 1:
            new Template(System.in).replaceStitches(readMatrix(args[0])).write(out);
            break;
        case 2:
            new Template(System.in).replaceBoth(readMatrix(args[0]), readMatrix(args[1])).write(out);
            break;
        default:
            showUsage();
            return;
        }
        System.out.write(out.toByteArray());
        // <dependency>
        // <groupId>batik</groupId>
        // <artifactId>batik-rasterizer</artifactId>
        // <version>1.6-1</version>
        // </dependency>

        // final TranscoderInput input = new TranscoderInput(new
        // ByteArrayInputStream(out.toByteArray()));
        // new PNGTranscoder().transcode(input, new TranscoderOutput(System.out));
    }

    private static String[][] readMatrix(final String arg) throws IOException
    {
        if (new File(arg).isFile())
            return Matrix.read(new FileInputStream(arg));
        else
            return Matrix.read(new ByteArrayInputStream(arg.getBytes()));
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
