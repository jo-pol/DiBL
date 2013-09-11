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
package dibl.p2t;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.JDOMException;

public class Generator
{
    private static String CFG = "cfg/"; // not final to allow WhiteboxTest
    private static final Map<String, TemplateDoc> templates = new HashMap<String, TemplateDoc>();
    private final PairTraversalPattern traversalPattern;
    private final TemplateDoc template;

    public static void buildSymetricVariants(final PairTraversalPattern primaryPattern, final File folder, final String... args) throws IOException, FileNotFoundException,
            JDOMException
    {
        final String[][] stitches = buildStitchesMatrix(args, primaryPattern.getNumberOfRows(), primaryPattern.getNumberOfColumns());
        final TemplateDoc template = readTemplate(primaryPattern.getNumberOfRows(), primaryPattern.getNumberOfColumns());
        int i = 0;
        for (final PairTraversalPattern variantPattern : new PatternProperties(primaryPattern).toPatterns())
        {
            applyCustomMix(template, stitches, variantPattern);
            template.write(new FileOutputStream(folder + "/" + (i++) + ".svg"));
        }
    }

    private static TemplateDoc readTemplate(final int numberOfRows, final int numberOfColumns) throws IOException, JDOMException, FileNotFoundException
    {
        return new TemplateDoc(new FileInputStream(CFG + "/" + numberOfRows + "x" + numberOfColumns + ".svg"));
    }

    private static String[][] buildStitchesMatrix(final String[] args, final int numberOfRows, final int numberOfColumns)
    {
        final String[][] stitches = new String[numberOfRows][numberOfColumns];
        for (int r = 0; r < stitches.length; r++)
            for (int c = 0; c < stitches[r].length; c++)
                stitches[r][c] = args[(r * stitches.length + c) % (args.length)];
        return stitches;
    }

    private static void applyCustomMix(final TemplateDoc template, final String[][] stitches, final PairTraversalPattern pattern)
    {
        for (final String cellID : pattern.getCellKeys())
        {
            final String tuple = pattern.getTuple(cellID);
            final String stitchType = stitches[cellID.charAt(1) - '1'][cellID.charAt(0) - 'A'];
            template.replaceClonesInBaseTile(cellID, stitchType, tuple);
        }
    }

    /**
     * @param in
     *        text version of a traversal pattern.
     * @param out
     *        an SVG file
     * @param stichType
     *        the prefix of a stitch label in the template with the same dimensions as the
     *        traversalPattern.
     * @throws IOException
     * @throws JDOMException
     *         in case of a problem with the template for a traversalPattern
     */
    public static void generate(final PairTraversalPattern traversalPattern, final PrintStream out, final String stichType) throws IOException, JDOMException
    {
        final TemplateDoc template = getTemplate(traversalPattern);

        for (final String cellID : traversalPattern.getCellKeys())
        {
            if (traversalPattern.isEmpty(cellID))
                template.setEmpty(cellID);
            else
            {
                final String tuple = traversalPattern.getTuple(cellID);
                template.replaceClonesInBaseTile(cellID, stichType, tuple);
            }
        }
        template.write(out);
    }

    /**
     * GeNerates one thread diagram for each traversalPattern.
     * 
     * @param stichTypes
     *        A matrix with at least the same dimensions as the traversalPatterns. Each cell should
     *        contain a prefix of a stitch label in the template with the same dimensions as the
     *        traversalPattern.
     * @param fileName
     *        A sequence number and svg extension will be added for each generated diagram.
     * @param traversalPatterns
     * @throws FileNotFoundException
     * @throws IOException
     * @throws JDOMException
     *         in case of a problem with the template for a traversalPattern
     */
    public static void generateCustomMix(final String[][] stichTypes, final String fileName, final PairTraversalPattern... traversalPatterns)
            throws FileNotFoundException, IOException, JDOMException
    {
        // fix the variants
        final TemplateDoc template = getTemplate(traversalPatterns[0]);
        int i = 0;
        for (final PairTraversalPattern tp : traversalPatterns)
        {
            for (int r = 0; r < tp.getNumberOfRows(); r++)
                for (int c = 0; c < tp.getNumberOfColumns(); c++)
                {
                    final String cellID = "ABCDEFGHIJKL".substring(c, c + 1) + (r + 1);
                    if (tp.isEmpty(cellID))
                        template.setEmpty(cellID);
                    else
                        template.replaceClonesInBaseTile(cellID, stichTypes[r][c], tp.getTuple(cellID));
                }
            writeVariation(fileName + "-" + (i++) + ".svg", template);
        }
    }

    /**
     * clears the cash of previously loaded templates.
     */
    public static void resetTemplates()
    {
        templates.clear();
    }

    /**
     * @param traversalPattern
     * @throws IOException
     * @throws JDOMException
     */
    public Generator(final PairTraversalPattern traversalPattern) throws IOException, JDOMException
    {
        this.traversalPattern = traversalPattern;
        template = getTemplate(traversalPattern);
    }

    /**
     * Creates numbered SVG files in the current directory. The number of files will the number of stitch
     * types multiplied with the number of non-zero tuples in the matrix of the traversal pattern.
     * 
     * @param folder
     *        TODO
     * @param stichTypes
     *        prefixes of stitch labels in the template with the same dimensions as the traversalPattern
     *        passed on to the constructor.
     * @throws IOException
     * @throws JDOMException
     */
    public void permutations(final File folder, final String... stichTypes) throws IOException, JDOMException
    {
        applyEmptyCells();
        final int nrOfCells = traversalPattern.getNumberOfColumns() * traversalPattern.getNumberOfRows();
        final String regexp = possibleVariations(stichTypes.length);
        for (int i = 0; i < Integer.MAX_VALUE; i++)
        {
            final String variation = pad(nrOfCells, Integer.toString(i, stichTypes.length));
            if (variation.length() > nrOfCells)
                break;
            if (!variation.matches(regexp))
                continue; // skip variations on empty vertexes

            applyVariation(variation, stichTypes);
            writeVariation(folder + "/" + variation + ".svg", template);
        }
    }

    private static TemplateDoc getTemplate(final PairTraversalPattern traversalPattern) throws FileNotFoundException, IOException, JDOMException
    {
        final String dimensions = traversalPattern.getDimensions();
        if (!templates.containsKey(dimensions))
            templates.put(dimensions, new TemplateDoc(new FileInputStream(CFG + dimensions + ".svg")));
        return templates.get(dimensions);
    }

    private void applyEmptyCells() throws FileNotFoundException, IOException, JDOMException
    {
        for (final String cellID : traversalPattern.getCellKeys())
        {
            if (traversalPattern.isEmpty(cellID))
                template.setEmpty(cellID);
        }
    }

    private void applyVariation(final String variation, final String... stichTypes) throws FileNotFoundException, IOException, JDOMException
    {
        int j = 0;
        for (final String cellID : traversalPattern.getCellKeys())
        {
            if (!traversalPattern.isEmpty(cellID))
            {
                final String tuple = traversalPattern.getTuple(cellID);
                final String stitchType = stichTypes[Integer.parseInt(variation.substring(j, j + 1))];
                template.replaceClonesInBaseTile(cellID, stitchType, tuple);
            }
            j++;
        }
    }

    private String pad(final int nrOfCells, final String variation)
    {
        if (variation.length() < nrOfCells)
            return "00000000000000000".substring(0, nrOfCells - variation.length()) + variation;
        return variation;
    }

    private String possibleVariations(final int length)
    {
        final StringBuffer regexp = new StringBuffer();
        for (final String cell : traversalPattern.getCellKeys())
        {
            if (traversalPattern.isEmpty(cell))
                regexp.append("0");
            else
                regexp.append("[" + "0123456789".substring(0, length) + "]");
        }
        return regexp.toString();
    }

    private static void writeVariation(final String fileName, final TemplateDoc template) throws FileNotFoundException, IOException, JDOMException
    {
        final FileOutputStream out = new FileOutputStream(fileName);
        try
        {
            template.write(out);
        }
        finally
        {
            out.close();
        }
    }
}
