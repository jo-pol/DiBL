package dibl.tools;

import java.text.MessageFormat;

public class Tile
{
    static final String FORMAT = "<g inkscape:label=\"{0} {1}\">\n{2}</g>\n";
    private String value;

    public Tile(Stitch stitch, Tuple tuple, String... components)
    {
        final StringBuffer sb = new StringBuffer();
        for (final String node : components)
            sb.append(node+"\n");
        value = MessageFormat.format(FORMAT, stitch, tuple, sb);
    }

    public String toString()
    {
        return value;
    }
}
