package dibl.math

trait Flipper[O] {
  def flipLeftRight(o: O): O

  def flipBottomUp(o: O): O

  def flipNW2SE(o: O): O

  def flipNE2SW(o: O): O
}
