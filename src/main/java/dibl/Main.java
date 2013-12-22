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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jdom2.JDOMException;

import dibl.diagrams.Template;
import dibl.math.Matrix;
import dibl.math.ShortTupleFlipper;

public class Main
{
    private static final String NEW_LINE = System.getProperty("line.separator");
    private Options options;
    private CommandLine commandLine;

    public static void main(final String... args) throws IOException, JDOMException, TranscoderException, ParseException
    {
        new Main(args);
    }

    private Main(final String... args) throws ParseException, IOException, JDOMException, TranscoderException
    {
        options = getOptions();
        commandLine = new BasicParser().parse(options, args);
        if (commandLine.hasOption("help"))
        {
            showUsage();
            return;
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        switch (commandLine.getArgList().size())
        {
        case 1:
            new Template(System.in).replaceStitches(readStitches()).write(out);
            break;
        case 2:
            new Template(System.in).replaceBoth(readStitches(), readTuples()).write(out);
            break;
        default:
            showUsage();
            return;
        }
        write(out.toByteArray());
    }

    private String[][] readStitches() throws IOException
    {
        return readMatrix(getArg(0));
    }

    private String[][] readTuples() throws IOException
    {
        String[][] tuples = readMatrix(getArg(1));
        if (commandLine.hasOption("x"))
            tuples = new Matrix<ShortTupleFlipper>(tuples, new ShortTupleFlipper()).flipNW2SE();
        if (commandLine.hasOption("y"))
            tuples = new Matrix<ShortTupleFlipper>(tuples, new ShortTupleFlipper()).flipNE2SW();
        return tuples;
    }

    private static String[][] readMatrix(final String arg) throws IOException
    {
        try
        {
            if (new File(arg).isFile())
                return Matrix.read(new FileInputStream(arg));
            else
                return Matrix.read(new ByteArrayInputStream(arg.getBytes()));
        }
        catch (final IOException e)
        {
            throw new IOException(arg + NEW_LINE + e.getMessage());
        }
        catch (final IllegalArgumentException e)
        {
            throw new IllegalArgumentException(arg + NEW_LINE + e.getMessage());
        }
        catch (final NullPointerException e)
        {
            throw new NullPointerException(arg + NEW_LINE + e.getMessage());
        }
    }

    private void write(final byte[] svg) throws IOException, TranscoderException
    {
        final ImageTranscoder transcoder = createTranscoder();
        if (transcoder == null)
            System.out.write(svg);
        else
        {
            final TranscoderOutput output = new TranscoderOutput(System.out);
            final TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(svg));
            transcoder.transcode(input, output);
        }
    }

    private ImageTranscoder createTranscoder() throws TranscoderException
    {
        final String ext = commandLine.getOptionValue("ext", "svg").toLowerCase();
        if (ext.equals("png"))
            return new PNGTranscoder();
        else if (ext.equals("tiff"))
            return new TIFFTranscoder();
        else if (ext.equals("jpg"))
        {
            final float quality = Float.parseFloat(commandLine.getOptionValue("q", "0.95"));
            final JPEGTranscoder transcoder = new JPEGTranscoder();
            transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, quality);
            return transcoder;
        }
        else if (!ext.equals("svg"))
        {
            System.err.println("unknown output type [" + ext + "] applied svg");
        }
        return null;
    }

    private void showUsage()
    {
        final String usage = "java -jar DiBL.jar [options] stitches [tuples]";
        final String footer = "rotate is flip along X + Y" + NEW_LINE + "stitches and tuples are either a file or a multiline string";
        final PrintStream saved = System.out;
        System.setOut(System.err);
        new HelpFormatter().printHelp(usage, "options:", options, footer);
        System.setOut(saved);
    }

    private static Options getOptions()
    {
        final Options options = new Options();
        options.addOption("help", false, "print this message");
        options.addOption("x", false, "flip tuples along x axis");
        options.addOption("y", false, "flip tuples along y axis");
        options.addOption("ext", true, "output type: svg, png, jpg, tiff. Default svg.");
        options.addOption("q", true, "jpg quality. Default 0.95 allowing 5% loss.");
        return options;
    }

    private String getArg(final int i)
    {
        return (String) commandLine.getArgList().get(i);
    }
}
