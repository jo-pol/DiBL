package dibl.math;

/**
 * Tuples define how nodes in a graph/diagram are connected. The schema's below show the numbered nodes
 * of the tuples wrapped counter clock wise around the central X-node. The methods accept as input both
 * the long version on the left, as the short version on the right. In both cases a long version is
 * returned.
 * 
 * <pre>
 *   3 2 1    2 - 1 
 *   4 X 0    3 X 0 
 *   5 6 7    4 - 5
 * </pre>
 * 
 * A tuple for a pair traversal diagram has two positive numbers for connections from nodes 0-4 to node
 * X, and two negative numbers for connections from node X to nodes 4-0. Remaining nodes are zero. <br>
 * As an example a long and and short tuple for an X: (0,1,0,1,0,-1,0,-1) (0,1,1,0,-1,-1) <br>
 * A + can only be expressed as a long tuple: (1,0,1,0,-1,0,-1,0)
 */
public class LongTupleFlipper implements Flipper<String>
{
    /** TupleElementFlipper */
    private static class TEF implements Flipper<String>
    {
        @Override
        public String flipBottomUp(final String value)
        {
            return "" + (-Integer.parseInt(value.trim()));
        }

        @Override
        public String flipLeftRight(final String value)
        {
            return value;
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

    private static final TEF tef = new TEF();

    private String[][] toMatrix(final String value)
    {
        final String[] s = value.replaceAll("[()]", "").split(",");
        if (s.length == 6)
            return new String[][] { {s[2], "0", s[1]}, {s[3], "0", s[0]}, {s[4], "0", s[5]}};
        else if (s.length == 8)
            return new String[][] { {s[3], s[2], s[1]}, {s[4], "0", s[0]}, {s[5], s[6], s[7]}};
        throw new IllegalArgumentException("tuple should containn 6 or 8 elements, got" + s.length);
    }

    private String toTuple(final String[][] s)
    {
        // 0.0 - 0.1 - 0.2
        // 1.0 - -X- - 1.2
        // 2.0 - 2.1 - 2.2
        return String.format("%s%s,%s,%s,%s,%s,%s,%s,%s%s", "(",//
                s[1][2], s[0][2], s[0][1], s[0][0], s[1][0], s[2][0], s[2][1], s[2][2], ")");
    }

    @Override
    public String flipBottomUp(final String value)
    {
        String[][] matrix = toMatrix(value);
        String[][] flipped = new Matrix<TEF>(matrix, tef).flipBottomUp();
        return toTuple(flipped);
    }

    @Override
    public String flipLeftRight(final String value)
    {
        String[][] matrix = toMatrix(value);
        String[][] flipped = new Matrix<TEF>(matrix, tef).flipLeftRight();
        return toTuple(flipped);
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
