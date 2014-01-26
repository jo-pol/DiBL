package dibl.tools;

import org.junit.Test;

public class TileTest
{
    private static final String[] IN = {"36,18 C 24,18", "36,0 C 28,8", "18,0 C 18,12", "0,0 C 8,8", "0,18 C 12,18", "-", "-", "-"};
    private static final String[] OUT = {"24,18 36,18", "-", "-", "-", "12,18 0,18", "8,28 0,36", "18,24 18,36", "28,28 36,36"};

    @Test
    public void preProtoTiles()
    {
        // outlines of the generated fat lines should be split and rearranged
        // as sketched in http://wiki.dibl.googlecode.com/git/automated.svg
        String stroke = "000000;stroke-width:8";
        for (Tuple tuple : Tuple.list())
        {
            for (Stitch stitch : Stitch.values())
            {
                final String line1 = new Line(stroke, IN[tuple.firstIn()], OUT[tuple.lastOut()]).toString();
                final String line2 = new Line(stroke, IN[tuple.lastIn()], OUT[tuple.firstOut()]).toString();
                System.out.println(new Tile(stitch, tuple, line1, line2));
            }
        }
    }

    @Test
    public void pinlessInterleaved()
    {
        for (Tuple tuple : Tuple.list())
        {
            for (Stitch stitch : Stitch.values())
                if (!stitch.hasPin())
                {
                    final String line1 = new Line(stitch.color, IN[tuple.firstIn()], OUT[tuple.firstOut()]).toString();
                    final String line2 = new Line(stitch.color, IN[tuple.lastIn()], OUT[tuple.lastOut()]).toString();
                    System.out.println(new Tile(stitch, tuple, line1, line2));
                }
        }
    }
}
