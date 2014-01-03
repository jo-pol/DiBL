package dibl.tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DiagonalColorCodedTileTest
{
    @Test
    public void generate()
    {
        for (ColorCodedTile.Stitch stitch : ColorCodedTile.Stitch.values())
        {
            String string = DiagonalColorCodedTile.generate(stitch);
            System.out.println(string);
            assertThat(string.split("\n").length, is(69 * (stitch.hasPin()?5:4)));
        }
    }
}
