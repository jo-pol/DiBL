package dibl.p2t;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class PatternPropertiesTest
{
    private static final String INPUT = "src/main/assembly/input";

    @Test
    public void unique() throws Exception
    {
        File[] files = new File(INPUT).listFiles();
        Comparator<File> comparator = new Comparator<File>()
        {
            @Override
            public int compare(File o1, File o2)
            {
                  return toInt(o1.getName()).compareTo(toInt(o2.getName()));
            }
        };
        Arrays.sort(files, comparator);
        Map<String, List<File>> signed = new TreeMap<String, List<File>>();
        Map<String, List<File>> unsigned = new TreeMap<String, List<File>>();
        for (final File file : files)
        {
            final PatternProperties pp = new PatternProperties(new PairTraversalPattern(new FileInputStream(file)));
            String s = pp.smallest();
            String u = pp.unsignedSmallest();
            if (!signed.containsKey(s))
                signed.put(s, new ArrayList<File>());
            signed.get(s).add(file);
            if (!unsigned.containsKey(u))
                unsigned.put(u, new ArrayList<File>());
            unsigned.get(u).add(file);
        }
        printLookALikes(unsigned.values());
        System.out.println(signed.size());
        System.out.println(unsigned.size());
    }

    private void printLookALikes(Collection<List<File>> values)
    {
        Set<String> lines = new TreeSet<String>(new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                {
                    return toInt(o1).compareTo(toInt(o2));
                }
            }
        });
        for (List<File> s : values)
            lines.add(Arrays.toString(s.toArray()).replaceAll(INPUT + "/", ""));
        for (String s : lines)
            System.out.println(s);
    }
    
    private Integer toInt(String o1)
    {
        return Integer.parseInt(o1.replaceAll("\\..*", "").replaceAll("[^_]*_",""));
    }
}
