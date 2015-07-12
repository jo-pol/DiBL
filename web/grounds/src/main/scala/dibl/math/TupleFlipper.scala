package dibl.math

/**
 * Tuples define how nodes in a graph/diagram are connected. The schema's below show the numbered nodes
 * of the tuples wrapped counter clock wise around the central X-node. The methods accept as input both
 * the long version on the left, as the short version on the right. In both cases a long version is
 * returned.
 *
 * <pre>
 * 3 2 1    2 - 1
 * 4 X 0    3 X 0
 * 5 6 7    4 - 5
 * </pre>
 *
 * A tuple for a pair traversal diagram has two positive numbers for connections from nodes 0-4 to node
 * X, and two negative numbers for connections from node X to nodes 4-0. Remaining nodes are zero. <br>
 * As an example a long and and short tuple for an X: (0,1,0,1,0,-1,0,-1) (0,1,1,0,-1,-1) <br>
 * A + can only be expressed as a long tuple: (1,0,1,0,-1,0,-1,0)
 */
object TupleFlipper extends Flipper[String] {

  def flipBottomUp(value: String): String = toTuple(new Matrix(toMatrix(value), tef).flipBottomUp)

  def flipLeftRight(value: String): String = toTuple(new Matrix(toMatrix(value), tef).flipLeftRight)

  def flipNW2SE(o: String): String = throw new UnsupportedOperationException

  def flipNE2SW(o: String): String = throw new UnsupportedOperationException

  private def toMatrix(value: String): Array[Array[String]] = {
    val s: Array[String] = value.replaceAll("[()]", "").split(",")
    if (s.length == 6)
      Array[Array[String]](Array(s(2), "0", s(1)), Array(s(3), "0", s(0)), Array(s(4), "0", s(5)))
    else if (s.length == 8)
      Array[Array[String]](Array(s(3), s(2), s(1)), Array(s(4), "0", s(0)), Array(s(5), s(6), s(7)))
    else
      throw new IllegalArgumentException(s"tuple should containn 6 or 8 elements, got ${s.length} elements: ($s)")
  }

  /** convert matrix back to tuple */
  private def toTuple(m: Array[Array[String]]) = s"${m(1)(2)},${m(0)(2)},${m(0)(1)},${m(0)(0)},${m(1)(0)},${m(2)(0)},${m(2)(1)},${m(2)(2)}"

  /** TupleElementFlipper */
  private class TEF extends Flipper[String] {
    def flipBottomUp(value: String): String = "" + (-value.trim.toInt)

    def flipLeftRight(value: String): String = value

    def flipNW2SE(o: String): String = throw new UnsupportedOperationException

    def flipNE2SW(o: String): String = throw new UnsupportedOperationException
  }
  private val tef = new TEF()
}
