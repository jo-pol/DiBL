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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.xpath.XPathExpressionException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class Template
{
    private static final Namespace NS_INKSCAPE = Namespace.getNamespace("inkscape", "http://www.inkscape.org/namespaces/inkscape");
    private static final Namespace NS_XLINK = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");
    private final Document doc;
    private final Map<String, Set<Element>> tileElements = new TreeMap<String, Set<Element>>();
    private final Map<String, String> idsByLabels = new TreeMap<String, String>();
    private final Map<String, String> labelsByIDs = new TreeMap<String, String>();
    private int nrOfRows = 1, nrOfCols = 1;

    /**
     * @param input
     *        SVG file
     * @throws IOException
     * @throws JDOMException
     * @throws XPathExpressionException
     */
    public Template(final InputStream input) throws IOException, JDOMException
    {
        doc = new SAXBuilder().build(input);
        collectTileElements();
        collectStitches();
    }

    public Template(final String input) throws IOException, JDOMException
    {
        final FileInputStream inputStream = new FileInputStream(input);
        try
        {
            doc = new SAXBuilder().build(inputStream);
            collectTileElements();
            collectStitches();
        }
        finally
        {
            inputStream.close();
        }
    }

    private void collectTileElements()
    {
        final XPathFactory f = XPathFactory.instance();
        // TODO extend xpath with condition inside for-loop
        final XPathExpression<Element> xpath = f.compile("//*[@inkscape:label='base tile']/*", Filters.element(), null, NS_INKSCAPE);
        for (final Element el : xpath.evaluate(doc))
        {
            if (el.getName().equals("use"))
            {
                final String cellID = el.getAttributeValue("label", NS_INKSCAPE);
                if (cellID != null && cellID.trim().length() > 0)
                {
                    if (!tileElements.containsKey(cellID.trim()))
                        tileElements.put(cellID.trim(), new HashSet<Element>());
                    tileElements.get(cellID.trim()).add((el));
                }
                final int r = cellID.toCharArray()[1] - '1';
                final int c = cellID.toCharArray()[0] - 'A';
                if (r >= getNrOfRows())
                    nrOfRows = r + 1;
                if (r >= getNrOfCols())
                    nrOfCols = c + 1;
            }
        }
    }

    private void collectStitches()
    {
        final XPathFactory f = XPathFactory.instance();
        final XPathExpression<Element> xpath = f.compile("//*[@inkscape:label='pile']/*", Filters.element(), null, NS_INKSCAPE);
        for (final Element el : xpath.evaluate(doc))
        {
            final String label = el.getAttributeValue("label", NS_INKSCAPE);
            final String id = el.getAttributeValue("id");
            idsByLabels.put(label, id);
            labelsByIDs.put("#" + id, label);
        }
    }

    public Template replaceStitches(final String[][] newValues)
    {
        replace(newValues, "^[^ ]*");
        return this;
    }

    public Template replaceBoth(final String[][] stitches, final String[][] tuples)
    {
        final String[][] newValues = new String[nrOfRows][nrOfCols];
        for (int r = 0; r < nrOfRows; r++)
            for (int c = 0; c < nrOfCols; c++)
                if (!tuples[r][c].matches("[(0,)]*"))
                    newValues[r][c] = stitches[r][c] + " " + tuples[r][c];
                else
                    newValues[r][c] = tuples[r][c];
        replace(newValues, "^.*$");
        return this;
    }

    private void replace(final String[][] newValues, final String searchPattern)
    {
        addAppliedMatrixToTextObject(newValues);

        for (final Set<Element> elements : tileElements.values())
        {
            for (final Element el : elements)
            {
                final char[] cellID = el.getAttribute("label", NS_INKSCAPE).getValue().toCharArray();
                final int r = cellID[1] - '1';
                final int c = cellID[0] - 'A';
                final String attribute = el.getAttribute("href", NS_XLINK).getValue();
                final String oldLabel = labelsByIDs.get(attribute);
                if (oldLabel != null)
                {
                    final String newLabel = oldLabel.replaceAll(searchPattern, newValues[r][c]);
                    final String newID = idsByLabels.get(newLabel);
                    if (newID != null)
                        el.setAttribute("href", "#" + newID, NS_XLINK);
                }
            }
        }
    }

    private void addAppliedMatrixToTextObject(final String[][] newValues)
    {
        final XPathFactory f = XPathFactory.instance();
        final XPathExpression<Element> xpath = f.compile("//*[@inkscape:label='trace']/*", Filters.element(), null, NS_INKSCAPE);
        List<Element> traceElements = xpath.evaluate(doc);
        if (!traceElements.isEmpty())
            traceElements.iterator().next().addContent(Arrays.deepToString(newValues));
    }

    public Map<String, Boolean> getEmptyCells()
    {
        final Map<String, Boolean> result = new TreeMap<String, Boolean>();
        for (final Set<Element> elements : tileElements.values())
        {
            final Element element = elements.iterator().next();
            final String href = element.getAttribute("href", NS_XLINK).getValue();
            final String tuple = labelsByIDs.get(href);
            final String cellID = element.getAttribute("label", NS_INKSCAPE).getValue();
            result.put(cellID, !tuple.contains("1"));
        }
        return result;
    }

    public void write(final OutputStream out) throws IOException
    {
        final XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(doc, out);
    }

    public int getNrOfRows()
    {
        return nrOfRows;
    }

    public int getNrOfCols()
    {
        return nrOfCols;
    }
}
