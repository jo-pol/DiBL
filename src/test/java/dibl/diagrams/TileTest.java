package dibl.diagrams;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import static dibl.diagrams.Tile.ColorCodedStitch.*;

public class TileTest
{
    @Test
    public void verify()
    {
        final String attributes = "style=\"fill:none;stroke:#00ff00\" inkscape:connector-curvature=\"0\" sodipodi:nodetypes=\"cc\"";
        final String expected = "<g inkscape:label=\"tc (0,1,0,0,1,0,-1,-1)\">\n" + //
                "<path d=\"M 36,0 C 28,8  18,24 18,36\" " + attributes + " />\n" + //
                "<path d=\"M 0,18 C 12,18  28,28 36,36\" " + attributes + " />\n" + //
                "</g>";
        assertThat(new Tile(tc, "(0,1,0,0,1,0,-1,-1)").toString(), is(expected));
    }

    @Test
    public void generate()
    {
        // -1,0,1,1,-1,0,0,0 -> 1221000
        final int max = Integer.parseInt("22222222", 3);
        for (int i = 0; i <= max; i++)
        {
            try
            {
                System.out.println(new Tile(tc, toTuple(Integer.toString(i, 3))));
            }
            catch (final IllegalArgumentException e)
            {
                // just skip
            }
            catch (final Exception e)
            {
                System.err.println(e.getMessage());
            }
        }
    }

    private String toTuple(final String s)
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
