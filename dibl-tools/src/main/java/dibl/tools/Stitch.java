package dibl.tools;

public enum Stitch
{
    tc("00ff00"), ctc("cc88ff"), tctc("ff0000"), tcptc("00ff00"), ctcpctc("cc88ff"), tctcptctc("ff0000");

    public final String color;

    Stitch(final String color)
    {
        this.color = color;
    }

    boolean hasPin()
    {
        return name().contains("p");
    }
}
