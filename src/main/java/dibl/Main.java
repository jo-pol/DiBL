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

import java.awt.Color;
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
    private static final Float JPG_QUALITY = 0.95f;
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
        final int nrOfArgs = commandLine.getArgList().size();
        if (commandLine.hasOption("help") || nrOfArgs < 1 || nrOfArgs > 2)
        {
            showUsage();
            return;
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final String[][] stitches = readStitches();
        switch (nrOfArgs)
        {
        case 1:
            new Template(System.in).replaceStitches(stitches).write(out);
            break;
        case 2:
            // error handling: process arguments before reading from standard input
            final String[][] tuples = readTuples();
            new Template(System.in).replaceBoth(stitches, tuples).write(out);
            break;
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
        {
            final PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_FORCE_TRANSPARENT_WHITE, true);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
            return transcoder;
        }
        else if (ext.equals("tiff"))
        {
            final TIFFTranscoder transcoder = new TIFFTranscoder();
            transcoder.addTranscodingHint(TIFFTranscoder.KEY_FORCE_TRANSPARENT_WHITE, true);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
            return transcoder;
        }
        else if (ext.equals("jpg"))
        {
            final float quality = Float.parseFloat(commandLine.getOptionValue("q", JPG_QUALITY.toString()));
            final JPEGTranscoder transcoder = new JPEGTranscoder();
            transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, quality);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
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
        final String header = "Transform a bobbin lace svg template into a variant." + NEW_LINE + "options:";
        final String footer = "Rotate is flip along X + Y." + NEW_LINE //
                + "The stitches and tuples arguments are either a file or a multiline string." + NEW_LINE //
                + "A tuples argument makes garbage diagrams of non pair travesal patterns." + NEW_LINE //
                + "Flipping applied to the wrong type of template cause garbage diagrams.";
        final PrintStream saved = System.out;
        System.setOut(System.err);
        new HelpFormatter().printHelp(usage, header, options, footer);
        System.setOut(saved);
    }

    private static Options getOptions()
    {
        final int loss = 100 - Math.round(JPG_QUALITY * 100);
        final String flipDiamondAlong = "flip tuples for a diamond tiled template along the ";
        final Options options = new Options();
        options.addOption("help", false, "print this message");
        options.addOption("x", false, flipDiamondAlong + "x axis");
        options.addOption("y", false, flipDiamondAlong + "y axis");
        options.addOption("ext", true, "output type: svg, png, jpg, tiff. Default svg.");
        options.addOption("q", true, "jpg quality. Default " + JPG_QUALITY + ", allowing " + loss + "% loss.");
        return options;
    }

    private String getArg(final int i)
    {
        return (String) commandLine.getArgList().get(i);
    }
}
