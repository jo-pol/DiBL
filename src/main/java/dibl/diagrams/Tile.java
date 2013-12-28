package dibl.diagrams;

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

    private static final String TILE = "<g inkscape:label=\"{0} {1}\">\n{2}\n{3}{4}\n</g>\n";
    private static final String LINE = "<path d=\"M {1} {3} {2}\" style=\"fill:none;stroke:#{0}\"" + //
            " inkscape:connector-curvature=\"0\" sodipodi:nodetypes=\"{4}\" />";
    private static final List<String> IN = Collections.unmodifiableList(Arrays.asList(//
            "36,18 C 24,18", "36,0 C 28,8", "18,0 C 18,12", "0,0 C 8,8", "0,18 C 12,18", "-", "-", "-"));
    private static final List<String> OUT = Collections.unmodifiableList(Arrays.asList(//
            "24,18 36,18", "-", "-", "-", "12,18 0,18", "8,28 0,36", "18,24 18,36", "28,28 36,36"));
    private static final List<String> MIRROR = Collections.unmodifiableList(Arrays.asList(//
            "12,18", "12,24", "18,24", "24,24", "24,18", "24,12", "18,12", "12,12"));
    private static final String PIN = "\n<path " + //
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
            pin = PIN;
            line1 = createLine(firstIn(), lastOut());
            line2 = createLine(lastIn(), firstOut());
            // TODO fix straight lines and lines along the same side of the pin
            // (-1, 1, 1, 0, 0, 0, 0, -1) (1, 1, 0, 0, 0, 0, -1, -1) look like ((
            // (0, 0, 1, 1, -1, -1, 0, 0) (0, 0, 0, 1, 1, -1, -1, 0) look like ))
        }
        else
        {
            pin = "";
            line1 = createSimpleLine(firstIn(), firstOut());
            line2 = createSimpleLine(lastIn(), lastOut());
        }
        return MessageFormat.format(TILE, stitch.name(), tuple, line1, line2, pin);
    }

    private String createSimpleLine(final int in, final int out)
    {
        return MessageFormat.format(LINE, stitch.color, IN.get(in), OUT.get(out), "", "cc");
    }

    private String createLine(final int in, final int out)
    {
        Point2D start = createPoint(MIRROR.get(out));
        Point2D end = createPoint(MIRROR.get(in));
        Point2D p = new Point2D.Double((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);
        String tangent = MIRROR.get(out) + " " + p.getX() + "," + p.getY() + " " + MIRROR.get(in);
        return MessageFormat.format(LINE, stitch.color, IN.get(in), OUT.get(out), tangent, "csc");
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

    public static String generate(final ColorCodedStitch stitch)
    {
        final StringBuffer sb = new StringBuffer();
        final int max = Integer.parseInt("22222222", 3);
        for (int i = 0; i <= max; i++)
        {
            Tile tile;
            try
            {
                tile = new Tile(stitch, toTuple(Integer.toString(i, 3)));
            }
            catch (final IllegalArgumentException e)
            {
                continue;
            }
            sb.append(tile.toString());
        }
        return sb.toString();
    }

    private static String toTuple(final String s)
    {
        final String padded = ("00000000" + s).substring(s.length());
        final int[] items = new int[8];
        for (int i = 0; i < 8; i++)
        {
            items[i] = Integer.parseInt(padded.substring(i, i + 1)) - 1;
        }
        return Arrays.toString(items).replace("[", "(").replace("]", ")");
    }
}
