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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * @param in
     *        text version of a traversal pattern.
     * @param out
     *        an SVG file
     * @param stichType
     *        the prefix of stitch labels in the template
     * @throws IOException
     * @throws JDOMException
     */
    public static void generate(final InputStream in, final PrintStream out, final String stichType) throws IOException, JDOMException
    {
        final PairTraversalPattern traversalPattern = new PairTraversalPattern(in);
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
     * clears the cash of previously loaded templates.
     */
    public static void resetTemplates()
    {
        templates.clear();
    }

    /**
     * @param in
     *        text version of a traversal pattern.
     * @throws IOException
     * @throws JDOMException
     */
    public Generator(final InputStream in) throws IOException, JDOMException
    {
        traversalPattern = new PairTraversalPattern(in);
        template = getTemplate(traversalPattern);
    }

    /**
     * Creates numbered SVG files in the current directory. The number of files will the number of stitch
     * types multiplied with the number of non-zero tuples in the matrix of the traversal pattern.
     * 
     * @param stichTypes
     *        prefixes of stitch labels in the template
     * @throws IOException
     * @throws JDOMException
     */
    public void variations(final String... stichTypes) throws IOException, JDOMException
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
            writeVariation(variation + ".svg");
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

    private void writeVariation(final String fileName) throws FileNotFoundException, IOException, JDOMException
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
