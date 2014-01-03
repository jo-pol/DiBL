package dibl.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiagonalColorCodedTile extends ColorCodedTile
{
    private static final List<String> IN = Collections.unmodifiableList(Arrays.asList(//
            "54,18 C 24,18", "36,0 C 28,8", "18,0 C 18,12", "0,0 C 8,8", "-18,18 C 12,18", "-", "-", "-"));
    private static final List<String> OUT = Collections.unmodifiableList(Arrays.asList(//
            "24,18 54,18", "-", "-", "-", "12,18 -18,18", "8,28 0,36", "18,24 18,36", "28,28 36,36"));

    public DiagonalColorCodedTile(final Stitch stitch, final String tuple)
    {
        super(stitch, tuple);
    }

    public String getIn(int i)
    {
        return IN.get(i);
    }

    public String getOut(int i)
    {
        return OUT.get(i);
    }

    public static String generate(final Stitch stitch)
    {
        final StringBuffer sb = new StringBuffer();
        final int max = Integer.parseInt("22222222", 3);
        for (int i = 0; i <= max; i++)
        {
            ColorCodedTile tile;
            try
            {
                tile = new DiagonalColorCodedTile(stitch, toTuple(Integer.toString(i, 3)));
            }
            catch (final IllegalArgumentException e)
            {
                continue;
            }
            sb.append(tile.toString());
        }
        return sb.toString();
    }
}
