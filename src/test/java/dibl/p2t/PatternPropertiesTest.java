package dibl.p2t;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import dibl.p2t.PairTraversalPattern;
import dibl.p2t.PatternProperties;

public class PatternPropertiesTest
{
    private static final String INPUT = "src/main/assembly/input";

    @Test
    public void unique() throws Exception
    {
        Map<String, File> signed = new TreeMap<String, File>();
        Map<String, File> unsigned = new TreeMap<String, File>();
        for (final File file : new File(INPUT).listFiles())
        {
            final PatternProperties pp = new PatternProperties(new PairTraversalPattern(new FileInputStream(file)));
            String s = pp.smallest();
            if (signed.containsKey(s))
                System.out.println(file.getName() + "\tduplicates " + unsigned.get(s).getName());
            else
            {
                signed.put(s, file);
                if (unsigned.containsKey(s))
                    System.out.println(file.getName() + "\tflips " + signed.get(s).getName());
                else
                    System.out.println(file.getName() + "\t" + pp.startPoints());
            }
            if (!unsigned.containsKey(s))
                unsigned.put(s, file);
        }
        System.out.println(signed.size());
        System.out.println(unsigned.size());
    }
}
