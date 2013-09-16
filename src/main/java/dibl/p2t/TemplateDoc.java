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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
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

public class TemplateDoc
{
    private static final String EMPTY_TUPLE = "(0,0,0,0,0,0)";
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
    public TemplateDoc(final InputStream input) throws IOException, JDOMException
    {
        doc = new SAXBuilder().build(input);
        collectTileElements();
        collectStitches();
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
                int r = cellID.toCharArray()[1] - '1';
                int c = cellID.toCharArray()[0] - 'A';
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

    public void replaceClonesInBaseTile(final String cellID, final String stichType, final String tuple)
    {
        if (!tileElements.containsKey(cellID))
            return;
        String stitchID = idsByLabels.get(stichType + " " + tuple);
        if (stitchID == null)
            stitchID = idsByLabels.get(EMPTY_TUPLE);
        for (final Element el : tileElements.get(cellID))
            el.setAttribute("href", "#" + stitchID, NS_XLINK);
    }

    public void replaceClonesInBaseTile(final String[][] stitchTypes)
    {
        for (final Set<Element> elements : tileElements.values())
        {
            for (final Element el : elements)
            {
                char[] cellID = el.getAttribute("label", NS_INKSCAPE).getValue().toCharArray();
                int r = cellID[1] - '1';
                int c = cellID[0] - 'A';
                String attribute = el.getAttribute("href", NS_XLINK).getValue();
                String oldLabel = labelsByIDs.get(attribute);
                if (oldLabel!=null&&!oldLabel.equals(EMPTY_TUPLE))
                {
                    String newLabel = oldLabel.replaceAll("[a-z]+", stitchTypes[r][c]);
                    el.setAttribute("href", "#" + idsByLabels.get(newLabel), NS_XLINK);
                }
            }
        }
    }

    public Map<String,Boolean> getEmptyCells()
    {
        Map<String,Boolean> result = new TreeMap<String,Boolean>();
        for (final Set<Element> elements : tileElements.values())
        {
            Element element = elements.iterator().next();
            String href = element.getAttribute("href", NS_XLINK).getValue();
            String tuple = labelsByIDs.get(href);
            String cellID = element.getAttribute("label", NS_INKSCAPE).getValue();
            result.put(cellID,EMPTY_TUPLE.equals(tuple));
        }
        return result;
    }

    public void setEmpty(final String cellID)
    {
        if (!tileElements.containsKey(cellID))
            return;
        final String stitchID = idsByLabels.get(EMPTY_TUPLE);
        if (stitchID == null)
            return;
        for (final Element el : tileElements.get(cellID))
            el.setAttribute("href", "#" + stitchID, NS_XLINK);
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
