package dibl.tools;

import org.junit.Test;

public class TupleTest
{
    @Test
    public void x()
    {
        for (Tuple t : Tuple.list())
            System.out.println(t);
    }
}
