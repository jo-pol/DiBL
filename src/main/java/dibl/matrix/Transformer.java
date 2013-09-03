package dibl.matrix;

public interface Transformer<O>
{
   O flipLeftRight(O o);
   O flipBotomUp(O o);
   O rotate180(O o);
}
