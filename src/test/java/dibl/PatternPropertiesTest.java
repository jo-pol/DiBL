package dibl;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;

public class PatternPropertiesTest
{
    private static final String INPUT = "src/main/assembly/input";

    @Test
    public void collectStarts() throws Exception
    {
        for (final File file : new File(INPUT).listFiles())
        {
            final PatternProperties t = new PatternProperties(new PairTraversalPattern(new FileInputStream(file)));
            if (t.startPoints().length() > 0)
                System.out.println(file.getName()+"\t" + t.startPoints());
        }
    }
}
