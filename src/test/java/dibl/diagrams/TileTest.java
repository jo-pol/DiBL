package dibl.diagrams;

import static dibl.diagrams.Tile.ColorCodedStitch.*;
import static dibl.diagrams.Tile.ColorCodedStitch.tc;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TileTest
{
    @Test
    public void verify()
    {
        final String attributes = "style=\"fill:none;stroke:#00ff00\" inkscape:connector-curvature=\"0\" sodipodi:nodetypes=\"cc\"";
        final String expected = "<g inkscape:label=\"tc (0,1,0,0,1,0,-1,-1)\">\n" + //
                "<path d=\"M 36,0 C 28,8  18,24 18,36\" " + attributes + " />\n" + //
                "<path d=\"M 0,18 C 12,18  28,28 36,36\" " + attributes + " />\n" + //
                "</g>\n";
        assertThat(new Tile(tc, "(0,1,0,0,1,0,-1,-1)").toString(), is(expected));
    }

    @Test
    public void ctc()
    {
        String string = Tile.generate(ctc);
        System.out.println(string);
        assertThat(string.split("\n").length, is(69 * 4));
    }

    @Test
    public void tcptc()
    {
        String string = Tile.generate(tcptc);
        System.out.println(string);
        assertThat(string.split("\n").length, is(69 * 5));
    }
}
