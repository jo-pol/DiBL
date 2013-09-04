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
        Arrays.sort(files, createFileComparator());

        Map<String, List<File>> signed = new TreeMap<String, List<File>>();
        Map<String, List<File>> unsigned = new TreeMap<String, List<File>>();
        for (final File file : files)
        {
            final PatternProperties pp = new PatternProperties(new PairTraversalPattern(new FileInputStream(file)));
            addToCollection(file, pp.smallest(), signed);
            addToCollection(file, pp.unsignedSmallest(), unsigned);
        }
        printLookALikes(unsigned.values());
        System.out.println(files.length+" files");
        System.out.println(signed.size()+" mathematically different patterns");
        System.out.println(unsigned.size()+" groups of identical/look-a-like patterns");
    }

    private void addToCollection(final File file, String key, Map<String, List<File>> fileLists)
    {
        if (!fileLists.containsKey(key))
            fileLists.put(key, new ArrayList<File>());
        fileLists.get(key).add(file);
    }

    private void printLookALikes(Collection<List<File>> values)
    {
        Set<String> lines = new TreeSet<String>(createStringComparator());
        for (List<File> s : values)
            lines.add(Arrays.toString(s.toArray()).replaceAll(INPUT + "/", ""));
        for (String s : lines)
            System.out.println(s);
    }

    private Comparator<File> createFileComparator()
    {
        return new Comparator<File>()
        {
            @Override
            public int compare(File o1, File o2)
            {
                return toInt(o1.getName()).compareTo(toInt(o2.getName()));
            }
        };
    }

    private Comparator<String> createStringComparator()
    {
        return new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                {
                    if (o1.substring(1, 2).equals(o2.substring(1, 2)))
                        return toInt(o1).compareTo(toInt(o2));
                    else
                        return o1.compareTo(o2);
                }
            }
        };
    }

    private Integer toInt(String o1)
    {
        return Integer.parseInt(o1.replaceAll("\\..*", "").replaceAll("[^_]*_", ""));
    }
}
