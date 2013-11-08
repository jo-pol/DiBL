package dibl.matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MatrixReader
{
    public static String[][] read(InputStream input) throws IOException
    {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try
        {
            final String[] dimensions = reader.readLine().split("\\s");
            int rows = Integer.parseInt(dimensions[0].trim());
            int cols = Integer.parseInt(dimensions[1].trim());
            String[][] m = new String[rows][cols];
            for (int r = 0; r < rows; r++)
            {
                final String[] tuples = reader.readLine().split("\t");
                for (int c = 0; c < cols && c < tuples.length; c++)
                {
                    if (tuples[c].length() > 0)
                        m[r][c] = tuples[c];
                    else
                        m[r][c] = "()";
                }
            }
            return m;
        }
        finally
        {
            reader.close();
        }
    }
}
