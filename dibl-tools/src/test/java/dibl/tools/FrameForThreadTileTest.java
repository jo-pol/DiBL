package dibl.tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FrameForThreadTileTest
{
    @Test
    public void generate()
    {
        final String string = FrameForThreadTile.generate(ColorCodedTile.Stitch.ctc);
        System.out.println(string);
        assertThat(string.split("\n").length, is(69 * (4)));
    }
}
