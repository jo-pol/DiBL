package dibl.p2t;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dibl.matrix.Transformer;

public class TupleTransformer implements Transformer<String>
{
    private static final List<String> tuples = Arrays.asList(new String[] {"(-1,0,1,1,-1,0)", "(-1,0,1,1,0,-1)", "(-1,1,0,1,-1,0)", "(-1,1,0,1,0,-1)",
            "(-1,1,1,-1,0,0)", "(-1,1,1,0,-1,0)", "(-1,1,1,0,0,-1)", "(0,0,0,0,0,0)", "(0,0,1,1,-1,-1)", "(0,1,0,1,-1,-1)", "(0,1,1,-1,-1,0)",
            "(0,1,1,-1,0,-1)", "(0,1,1,0,-1,-1)", "(1,0,0,1,-1,-1)", "(1,0,1,-1,-1,0)", "(1,0,1,-1,0,-1)", "(1,0,1,0,-1,-1)", "(1,1,0,-1,-1,0)",
            "(1,1,0,-1,0,-1)", "(1,1,0,0,-1,-1)",});
    private static final Map<String, String> flippedLeftRight, flippedBottomUp, rotated180;
    static
    {
        flippedLeftRight = new HashMap<String, String>();
        flippedBottomUp = new HashMap<String, String>();
        rotated180 = new HashMap<String, String>();
        for (String t : tuples)
        {
            String s[] = t.replaceAll("[()]", "").split(",");
            flippedLeftRight.put(t, fmt(s[3], s[2], s[1], s[0], s[5], s[4]));
            rotated180.put(t, fmt(s[3], inv(s[4]), inv(s[5]), s[0], inv(s[1]), inv(s[2])));
            flippedBottomUp.put(t, fmt(s[0], inv(s[5]), inv(s[4]), s[3], inv(s[2]), inv(s[1])));
        }
    }

    public String flipLeftRight(String value)
    {
        if (!tuples.contains(value))
            throw new IllegalArgumentException();
        return flippedLeftRight.get(value);
    }

    public String flipBotomUp(String value)
    {
        if (!tuples.contains(value))
            throw new IllegalArgumentException();
        return flippedBottomUp.get(value);
    }

    public String rotate180(String value)
    {
        if (!tuples.contains(value))
            throw new IllegalArgumentException();
        return rotated180.get(value);
    }

    private static String inv(String v)
    {
        if (v.equals("0"))
            return v;
        if (v.startsWith("-"))
            return v.substring(1);
        return "-" + v;
    }

    private static String fmt(String... args)
    {
        return String.format("(%s,%s,%s,%s,%s,%s)",(Object[])args);
    }
}
