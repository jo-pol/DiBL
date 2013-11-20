package dibl.math;

public class LongTupleFlipper implements Flipper<String>
{
    private String[][] toMatrix(final String value)
    {
        final String[] s = value.replaceAll("[()]", "").split(",");
        if (s.length == 6)
            return new String[][] { {s[2], "0", s[1]}, {s[3], "", s[0]}, {s[4], "0", s[5]}};
        else if (s.length == 8)
            return new String[][] { {s[3], s[2], s[1]}, {s[4], "", s[0]}, {s[5], s[6], s[7]}};
        throw new IllegalArgumentException("tuple should containn 6 or 8 elements, got" + s.length);
    }

    private String toTuple(final String[][] s)
    {
        return String.format("%s%s,%s,%s,%s,%s,%s,%s,%s%s", "(",s[1][2], s[0][2], s[0][1], s[0][0], s[1][0], s[2][0], s[2][1], s[2][2], ")");
    }

    @Override
    public String flipBottomUp(final String value)
    {
        String[][] in = toMatrix(value);
        return toTuple(new String[][] { {inv(in[2][0]), inv(in[2][1]), inv(in[2][2])}, //
                {inv(in[1][0]), inv(in[1][1]), inv(in[1][2])}, //
                {inv(in[0][0]), inv(in[0][1]), inv(in[0][2])}});
    }

    private static String inv(String v)
    {
        if (v.equals("0"))
            return v;
        if (v.startsWith("-"))
            return v.substring(1);
        return "-" + v;
    }

    @Override
    public String flipLeftRight(final String value)
    {
        String[][] in = toMatrix(value);
        return toTuple(new String[][] { {in[0][2], in[0][1], in[0][0]}, //
                {in[1][2], in[1][1], in[1][0]}, //
                {in[2][2], in[2][1], in[2][0]}});
    }

    @Override
    public String flipNW2SE(String o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String flipNE2SW(String o)
    {
        throw new UnsupportedOperationException();
    }
}
