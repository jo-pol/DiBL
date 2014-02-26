package dibl.grids.polar;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.junit.Test;

public class PolarGridTest
{
    @Test
    public void printDefault() throws Exception
    {
        final PrintWriter pw = new PrintWriter(new FileOutputStream("target/grid.svg"));
        new PolarGrid().printDoc(pw);
        pw.close();
    }
}
