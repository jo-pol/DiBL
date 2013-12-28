package dibl.diagrams;

import static dibl.diagrams.ColorCodedTile.Stitch.tc;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ColorCodedTileTest
{
    @Test
    public void verify()
    {
        final String attributes = "style=\"fill:none;stroke:#00ff00\" inkscape:connector-curvature=\"0\" sodipodi:nodetypes=\"cc\"";
        final String expected = "<g inkscape:label=\"tc (0,1,0,0,1,0,-1,-1)\">\n" + //
                "<path d=\"M 36,0 C 28,8  18,24 18,36\" " + attributes + " />\n" + //
                "<path d=\"M 0,18 C 12,18  28,28 36,36\" " + attributes + " />\n" + //
                "</g>\n";
        assertThat(new ColorCodedTile(tc, "(0,1,0,0,1,0,-1,-1)").toString(), is(expected));
    }

    @Test
    public void generate()
    {
        for (ColorCodedTile.Stitch stitch : ColorCodedTile.Stitch.values())
        {
            String string = ColorCodedTile.generate(stitch);
            System.out.println(string);
            assertThat(string.split("\n").length, is(69 * (stitch.hasPin()?5:4)));
        }
    }
}
