package dibl.tools;

import java.text.MessageFormat;

public class Line
{
    static final String FORMAT = "<path" + //
            " d=\"M {0}\"" + //
            " style=\"fill:none;stroke:#{1}\"" + //
            " inkscape:connector-curvature=\"0\" sodipodi:nodetypes=\"{2}\" />";
    private final String value;

    public Line(final String stroke, final String... nodes)
    {
        if (nodes.length < 2 || nodes.length > 3)
            throw new IllegalArgumentException("expecting two corder nodes and an optional smooth node in between");
        final StringBuffer sb = new StringBuffer();
        final String nodeTypes = nodes.length == 2 ? "cc" : "csc";
        for (final String node : nodes)
            sb.append(node+" ");
        value = MessageFormat.format(FORMAT, sb, stroke, nodeTypes);
    }

    public String toString()
    {
        return value;
    }
}
