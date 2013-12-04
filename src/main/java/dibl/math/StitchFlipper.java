package dibl.math;

public class StitchFlipper implements Flipper<String>
{
    @Override
    public String flipLeftRight(String o)
    {
        return o;
    }

    @Override
    public String flipBottomUp(String o)
    {
        return o;
    }

    @Override
    public String flipNW2SE(String o)
    {
        return o;
    }

    @Override
    public String flipNE2SW(String o)
    {
        return o;
    }
}
