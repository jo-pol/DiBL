// @formatter:off
/*
 * Copyright 2013, J. Pol
 *
 * This file is part of free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation.
 *
 * This package is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details. A copy of the GNU General Public License is
 * available at <http://www.gnu.org/licenses/>.
 */
// @formatter:on
package dibl.p2t;

import static org.junit.Assert.assertTrue;

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

import org.junit.Ignore;
import org.junit.Test;

public class PatternPropertiesTest
{
    private static final String INPUT = "src/main/assembly/input";

    @Test
    public void flipTwiceForOriginal() throws Exception
    {
        File[] files = new File(INPUT).listFiles();
        for (final File file : files)
        {
            PairTraversalPattern pattern1 = new PairTraversalPattern(new FileInputStream(file));
            final PatternProperties props1 = new PatternProperties(pattern1);
            PatternProperties props2 = new PatternProperties(new PatternProperties(props1.toLeftRightPattern()).toLeftRightPattern());
            assertTrue(file.getName(), props1.smallest().equals(props2.smallest()));
            PatternProperties props3 = new PatternProperties(new PatternProperties(props1.toBottomUpPattern()).toBottomUpPattern());
            assertTrue(file.getName(), props1.smallest().equals(props3.smallest()));
            PatternProperties props4 = new PatternProperties(new PatternProperties(props1.toRotatedPattern()).toRotatedPattern());
            assertTrue(file.getName(), props1.smallest().equals(props4.smallest()));
            // flip along both axes is a rotation
            PatternProperties props5 = new PatternProperties(new PatternProperties(props1.toBottomUpPattern()).toLeftRightPattern());
            assertTrue(file.getName(), props5.smallest().equals(new PatternProperties(props1.toRotatedPattern()).smallest()));
        }
    }

    @Ignore //don't break the build with not commited input 
    @Test
    public void searchForFlippedVersions() throws Exception
    {
        int[] nrs = {408,618,687,717,1132,1199};
        System.out.println();
        for (Integer i:nrs)
        {
            PairTraversalPattern pattern = new PairTraversalPattern(new FileInputStream(INPUT+"/4x4_"+i+".txt"));
            System.out.println(i+" "+new PatternProperties(pattern).smallest());
        }
        System.out.println();
        PatternProperties props = new PatternProperties(new PairTraversalPattern(new FileInputStream(INPUT+"/4x4_"+nrs[0]+".txt")));
        System.out.println(new PatternProperties(props.toLeftRightPattern()).smallest());
        System.out.println(new PatternProperties(props.toBottomUpPattern()).smallest());
        System.out.println(new PatternProperties(props.toRotatedPattern()).smallest());
        System.out.println();
    }

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
            if (!signed.containsKey(pp.smallest()))
                addToCollection(file, pp.unsignedSmallest(), unsigned);
            addToCollection(file, pp.smallest(), signed);
        }
        printLookALikes(unsigned.values());
        System.out.println(files.length + " files");
        System.out.println(signed.size() + " mathematically different patterns");
        System.out.println(unsigned.size() + " groups of look-a-like patterns");
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
