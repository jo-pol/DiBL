package dibl.diagrams;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tile
{
    enum ColorCodedStitch
    {
        tc("00ff00"), ctc("ff8c00"), tctc("ff0000"), tcptc("00ff00"), ctcpctc("ff8c00"), tctcptctc("ff0000");

        private String color;

        ColorCodedStitch(final String color)
        {
            this.color = color;
        }

        boolean hasPin()
        {
            return name().contains("p");
        }
    }

    // stripped example
    //
    // <g inkscape:label="tctc (0,1,0,0,1,0,-1,-1)">
    // <path d="M 36,0 C 28,8 18,24 18,36" style="fill:none;stroke:#ff0000" />
    // <path d="m 0,18 c 12,0 28,10 36,18" style="fill:none;stroke:#ff0000" />
    // </g>
    private static final String TILE = "<g inkscape:label=\"{0} {1}\">\n{2}\n{3}{4}\n</g>";
    private static final String LINE = "<path d=\"M {1} {3} {2}\" style=\"fill:none;stroke:#{0}\"" + //
            " inkscape:connector-curvature=\"0\" sodipodi:nodetypes=\"{4}\" />";
    private static final List<String> IN = Collections.unmodifiableList(Arrays.asList(//
            "36,18 C 24,18", "36,0 C 28,8", "18,0 C 18,12", "0,0 C 8,8", "0,18 C 12,18", "-", "-", "-"));
    private static final List<String> OUT = Collections.unmodifiableList(Arrays.asList(//
            "24,18 36,18", "-", "-", "-", "12,18 0,18", "8,28 0,36", "18,24 18,36", "28,28 36,36"));
    private static final String PIN = "<path " + //
            "d='m 390,447.36218 a 5,5 0 1 1 -0.003,-0.16338' " + //
            "sodipodi:cx='385' " + //
            "sodipodi:cy='447.36218' " + //
            "sodipodi:end='6.2505032' " + //
            "transform='matrix(0.22506009,0,0,0.24208942,-68.648135,-90.301651)' " + //
            "sodipodi:open='true' " + //
            "sodipodi:start='0' " + //
            "sodipodi:ry='5' " + //
            "sodipodi:rx='5' " + //
            "style='fill:#000000;fill-opacity:1;' " + //
            "sodipodi:type='arc' " + //
            "/>";

    private final ColorCodedStitch stitch;
    private final List<Integer> tupleList = new ArrayList<Integer>();
    private final String tuple;

    public Tile(final ColorCodedStitch stitch, final String tuple)
    {
        this.stitch = stitch;
        this.tuple = tuple;

        for (final String i : tuple.replaceAll("[()]", "").trim().split(","))
            tupleList.add(Integer.parseInt(i.trim()));
        tupleList.add(tupleList.get(0));

        if (nrOfZeros() != 4 || firstIn() == -1 || firstOut() == -1 || lastIn() == -1 || lastOut() == -1)
            throw new IllegalArgumentException(tuple);
    }

    private int nrOfZeros()
    {
        int count = 0;
        for (int i = 0; i < 8; i++)
            if (tupleList.get(i) == 0)
                count++;
        return count;
    }

    public String toString()
    {
        final String pin;
        final String line1;
        final String line2;
        if (stitch.hasPin())
        {
            pin = "\n" + PIN;
            line1 = createLine(IN.get(firstIn()), OUT.get(lastOut()));
            line2 = createLine(IN.get(lastIn()), OUT.get(firstOut()));
        }
        else
        {
            pin = "";
            line1 = createLine(IN.get(firstIn()), OUT.get(firstOut()));
            line2 = createLine(IN.get(lastIn()), OUT.get(lastOut()));
        }
        return MessageFormat.format(TILE, stitch.name(), tuple, line1, line2, pin);
    }

    private String createLine(final String in, final String out)
    {
        try
        {
            new Line2D.Double(createPoint(in.split(" ")[0]), createPoint(out.split(" ")[1]));
            // TODO rotate line around (18,18), shrink with same center
        }
        catch (IllegalArgumentException e)
        {
        }
        if (in.trim().length()==0||out.trim().length()==0||in.equals("-")||out.equals("-"))
            throw new IllegalStateException(tuple + "[" + firstIn() + ";" + firstOut() + " " + lastIn() + ";" + lastOut() + "]" );
        final String controlPoint = stitch.hasPin() ? "18,18 18,18 18,18" : "";
        final String nodeTypes = stitch.hasPin() ? "csc" : "cc";
        return MessageFormat.format(LINE, stitch.color, in, out, controlPoint, nodeTypes);
    }

    private Point2D.Double createPoint(final String coordinates)
    {
        final String[] strings = coordinates.split(",");
        final double x = Double.parseDouble(strings[0]);
        final double y = Double.parseDouble(strings[1]);
        return new Point2D.Double(x, y);
    }

    private int firstIn()
    {
        for (int i = 0; i < 5; i++)
            if (tupleList.get(i) > 0)
                return i;
        return -1;
    }

    private int lastIn()
    {
        for (int i = firstIn() + 1; i < 5; i++)
            if (tupleList.get(i) > 0)
                return i;
        return -1;
    }

    private int firstOut()
    {
        for (int i = 4; i < 9; i++)
            if (tupleList.get(i) < 0)
                return i;
        return -1;
    }

    private int lastOut()
    {
        for (int i = firstOut() + 1; i < 9; i++)
            if (tupleList.get(i) < 0)
                return i % 8;
        return -1;
    }
}
