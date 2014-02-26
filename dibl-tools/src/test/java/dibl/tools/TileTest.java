package dibl.tools;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.junit.Test;

public class TileTest
{
    private static final String[] IN = {"36,18 C 24,18", "36,0 C 28,8", "18,0 C 18,12", "0,0 C 8,8", "0,18 C 12,18", "-", "-", "-"};
    private static final String[] OUT = {"24,18 36,18", "-", "-", "-", "12,18 0,18", "8,28 0,36", "18,24 18,36", "28,28 36,36"};

    @Test
    public void preProtoTiles() throws Exception
    {
        /*
         * To examine the results in InkScape: align all objects (ctrl-shift-a) to the north and west,
         * then spread them over rows and columns. Create outlines: select the node tool, select all
         * paths and apply stroke-to-path (ctrl-alt-c). However, the paths are closed and don't have the
         * desired number of nodes as sketched in
         * https://code.google.com/p/dibl/wiki/NewStitches#Thread_diagrams
         */
        String stroke = "000000;stroke-width:8";
        final PrintWriter os = new PrintWriter(new FileOutputStream("target/preProtoTiles.svg"));
        os.print(SvgDoc.HEAD);
        for (Tuple tuple : Tuple.list())
        {
            for (Stitch stitch : Stitch.values())
            {
                final String line1 = new Line(stroke, IN[tuple.firstIn()], OUT[tuple.lastOut()]).toString();
                final String line2 = new Line(stroke, IN[tuple.lastIn()], OUT[tuple.firstOut()]).toString();
                os.println(new Tile(stitch, tuple, line1, line2));
            }
        }
        os.print(SvgDoc.TAIL);
        os.close();
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
