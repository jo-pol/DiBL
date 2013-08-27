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
    private final Map<String, Set<String>> stitchTypesByTuple = new TreeMap<String, Set<String>>();

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
        collectStitcheTypes();
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
                final String value = el.getAttributeValue("label", NS_INKSCAPE);
                if (value != null && value.trim().length() > 0)
                {
                    if (!tileElements.containsKey(value.trim()))
                        tileElements.put(value.trim(), new HashSet<Element>());
                    tileElements.get(value.trim()).add((el));
                }
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
        }
    }

    private void collectStitcheTypes()
    {
        for (final String label : idsByLabels.keySet())
        {
            if (label.contains(" "))
            {
                final String[] strings = label.split(" ");
                final String stitchType = strings[0].trim();
                final String tuple = strings[1].trim();
                if (!stitchTypesByTuple.containsKey(tuple))
                    stitchTypesByTuple.put(tuple, new HashSet<String>());
                stitchTypesByTuple.get(tuple).add(stitchType);
            }
        }
    }

    public Set<String> getStitchTypes(final String tuple) throws JDOMException
    {
        return stitchTypesByTuple.get(tuple);
    }

    public void replaceClonesInBaseTile(final String cellID, final String stichType, final String tuple)
    {
        if (!tileElements.containsKey(cellID))
            return;
        final String stitchID = idsByLabels.get(stichType + " " + tuple);
        if (stitchID == null)
            return;
        for (final Element el : tileElements.get(cellID))
            el.setAttribute("href", "#" + stitchID, NS_XLINK);
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
}
