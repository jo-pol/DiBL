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

import dibl.diagrams.DPTP;
import dibl.diagrams.Template;
import dibl.math.LongTupleFlipper;
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
        parseCommandLine(args);
        if (commandLine.hasOption("help") || !commandLineIsValid())
        {
            showUsage();
            return;
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final String[][] stitches = readMatrix(getArg(0));
        if (commandLine.getArgList().size() == 1)
            new Template(System.in).replaceStitches(stitches).write(out);
        else
        {
            // error handling: process arguments before reading from standard input
            final String[][] tuples = readTuples();
            new Template(System.in).replaceBoth(stitches, tuples).write(out);
        }
        write(out.toByteArray());
    }

    private String[][] readTuples() throws IOException
    {
        String[][] tuples = readMatrix(getArg(1));
        // lower case for diamond tiled patterns
        if (commandLine.hasOption("x"))
            tuples = new Matrix<ShortTupleFlipper>(tuples, new ShortTupleFlipper()).flipNW2SE();
        if (commandLine.hasOption("y"))
            tuples = new Matrix<ShortTupleFlipper>(tuples, new ShortTupleFlipper()).flipNE2SW();
        // upper case for brick/checkerboard tiled patterns
        if (commandLine.hasOption("X"))
            tuples = new DPTP(tuples).flipBottomUp();
        if (commandLine.hasOption("Y"))
            tuples = new DPTP(tuples).flipLeftRight();
        // H/V for interleaved (brick/checkerboard) tiled patterns
        if (commandLine.hasOption("V"))
            tuples = new Matrix<LongTupleFlipper>(tuples, new LongTupleFlipper()).flipBottomUp();
        if (commandLine.hasOption("H"))
            tuples = new Matrix<LongTupleFlipper>(tuples, new LongTupleFlipper()).flipLeftRight();
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
            transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
            return transcoder;
        }
        else if (ext.equals("tiff"))
        {
            final TIFFTranscoder transcoder = new TIFFTranscoder();
            transcoder.addTranscodingHint(TIFFTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
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
        final String jarFileName = new java.io.File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        final String usage = "java -jar " + jarFileName + " [options] stitches [tuples]";
        final String header = "Transform a bobbin lace svg template into a variant." + NEW_LINE + "options:";
        final String footer = "Rotate is flip along X + Y." + NEW_LINE //
                + "The stitches and tuples arguments are either a file or a multiline string." + NEW_LINE //
                + "Tuples argument make garbage diagrams of non pair travesal templates.";
        final PrintStream saved = System.out;
        System.setOut(System.err);
        new HelpFormatter().printHelp(usage, header, options, footer);
        System.setOut(saved);
    }

    private void parseCommandLine(final String... args) throws ParseException
    {
        final int loss = 100 - Math.round(JPG_QUALITY * 100);
        final String flipDiamondAlong = "flip tuples for a diamond tiled template along the ";
        final String flipBrickAlong = "flip tuples for a brick or checkboard tiled template along the ";
        final String flipInterleavedAlong = "flip tuples for anterleaved template along the ";
        options = new Options();
        options.addOption("help", false, "print this message");
        options.addOption("x", false, flipDiamondAlong + "x axis");
        options.addOption("y", false, flipDiamondAlong + "y axis");
        options.addOption("X", false, flipBrickAlong + "x axis");
        options.addOption("Y", false, flipBrickAlong + "y axis");
        options.addOption("V", false, flipInterleavedAlong + "x axis");
        options.addOption("H", false, flipInterleavedAlong + "y axis");
        options.addOption("ext", true, "output type: svg, png, jpg, tiff. Default svg.");
        options.addOption("q", true, "jpg quality. Default " + JPG_QUALITY + ", allowing " + loss + "% loss.");
        commandLine = new BasicParser().parse(options, args);
    }

    private boolean commandLineIsValid()
    {
        final int nrOfArgs = commandLine.getArgList().size();
        if (nrOfArgs < 1)
        {
            System.err.println("need at least a stitches argument (filename or mutliline string)");
            return false;
        }
        if (commandLine.getArgList().size() > 2)
        {
            System.err.println("at most two arguments expected");
            return false;
        }
        if ((hasAnOptionOf("x", "y") && hasAnOptionOf("X", "Y", "V", "H")) //
                || (hasAnOptionOf("X", "Y") && hasAnOptionOf("x", "y", "V", "H")))
        {
            System.err.println("mixed types of flip specified");
            return false;
        }
        if (hasAnOptionOf("x", "y","X","Y","H","V") && nrOfArgs < 2)
        {
            System.err.println("need a second argument for tuples to flip");
            return false;
        }
        return true;
    }

    private boolean hasAnOptionOf(String... options)
    {
        for (String option : options)
            if (commandLine.hasOption(option))
                return true;
        return false;
    }

    private String getArg(final int i)
    {
        return (String) commandLine.getArgList().get(i);
    }
}
