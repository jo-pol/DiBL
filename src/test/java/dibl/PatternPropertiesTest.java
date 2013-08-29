package dibl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class PatternPropertiesTest
{
    private static final String INPUT = "src/main/assembly/input";

    @Test
    public void unique() throws Exception
    {
        Set<String> all = new TreeSet<String>();
        for (final File file : new File(INPUT).listFiles())
        {
            final PatternProperties pp = new PatternProperties(new PairTraversalPattern(new FileInputStream(file)));
            String s = pp.smallest();
            if (!all.contains(s))
            {
                System.out.println(file.getName() + "\t" + pp.startPoints());
                all.add(s);
            }
        }
        System.out.println(all.size());
    }
}
