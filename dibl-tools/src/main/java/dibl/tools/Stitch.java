package dibl.tools;

public enum Stitch
{
    tc("00ff00"), ctc("cc88ff"), tctc("ff0000"), tcptc("00ff00"), ctcpctc("cc88ff"), tctcptctc("ff0000");

    public final String color;

    /** Number of segments for the four more or less parallel lines of a ProtoTile.
      * A thread drawing of a stitch visually reconnects these segments with their neighbours.
      * ASCII-art shows what happens for tc alias twist-cross.
      * End nodes of outside segments exchange positions with end nodes of inside segements for each twist (t, l, r).
      * End nodes of inside segments exchange positions for each cross (c).
      * The tangents of the reconnected inside nodes rotate by 45 degrees.
      * <pre>
      *  3  4  1  2  Z-order
      *  |  |  |  |
      *  |  |  |  |
      *  |  .  .  |
      *  |  |  |  |
      *  |  |  |  |
      *  3  4  1  2  Z-order
      *
      *  becomes
      *
      *  3   4   1   2  Z-order
      *  \   /   \   /
      *   \ /     \ /
      *    /       /
      *   / \     / \
      *  /   .   .   \
      *  |    \ /    |
      *  |     \     |
      *  |    / \    |
      *  |   /   \   |
      *  4   1'  4'  1  Z-order
      * </pre>
      **/
    public final int segmentsLeft, segmentsRight, segmentsCenter;

    Stitch(final String color)
    {
        this.color = color;
        segmentsLeft = name().replaceAll("[^lt]","").length();
        segmentsRight = name().replaceAll("[^rt]","").length();
        segmentsCenter = name().replaceAll("[^c]","").length();
    }

    boolean hasPin()
    {
        return name().contains("p");
    }
}
