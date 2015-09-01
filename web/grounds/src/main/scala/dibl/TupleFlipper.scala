/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/

package dibl

import dibl.TupleFlipper.flipper

/**
 * Tuples define how nodes in a graph/diagram are connected. The schema's below show the numbered nodes
 * of the tuples wrapped counter clock wise around the central X-node.
 * The long tuple version on the left, the short on the right.
 *
 * <pre>
 * 3 2 1    2 - 1
 * 4 X 0    3 X 0
 * 5 6 7    4 - 5
 * </pre>
 *
 * A tuple for a pair traversal diagram has two positive numbers for connections from nodes 0-4 to node
 * X, and two negative numbers for connections from node X to nodes 4-0. Remaining nodes are zero. <br>
 * As an example a long and and short tuple for an X: (0,1,0,1,0,-1,0,-1) (0,1,1,0,-1,-1);
 * orientation is always top down <br>
 * A + can only be expressed as a long tuple, either oriented left to right (1,0,1,0,-1,0,-1,0),
 * or rigth to left (-1,0,1,0,1,0,-1,0)
 */
abstract class TupleFlipper extends Flipper[String] {

  def flipBottomUp(value: String): String = toTuple(flipper.flipBottomUp(toMatrix(value)))

  def flipLeftRight(value: String): String = toTuple(flipper.flipLeftRight(toMatrix(value)))

  /** Flips actually along the X axis to match the flip of a skewed matrix. */
  def flipNW2SE(value: String): String = toTuple(flipper.flipBottomUp(toMatrix(value)))

  /** Flips actually along the Y axis to match the flip of a skewed matrix. */
  def flipNE2SW(value: String): String = toTuple(flipper.flipLeftRight(toMatrix(value)))

  protected def toMatrix(value: String): M
  protected def toTuple(m: M): String
}

private object TupleFlipper {

  /** Flips tuples represented as matrices */
  private val flipper = new MatrixFlipper(new Flipper[String] {
    /** Change the sign of a tuple element */
    def flipBottomUp(value: String): String = (-value.trim.toInt).toString
    def flipLeftRight(value: String): String = value
    def flipNW2SE(value: String): String = throw new UnsupportedOperationException
    def flipNE2SW(value: String): String = throw new UnsupportedOperationException
  })
}

object LongTupleFlipper extends TupleFlipper {

  override def toMatrix(value: String): M = {
    val s: Array[String] = value.replaceAll("[()]", "").split(",")
    M(Array(s(3), s(2), s(1)), Array(s(4), "0", s(0)), Array(s(5), s(6), s(7)))
  }
  override def toTuple(m: M) = s"${m(1)(2)},${m(0)(2)},${m(0)(1)},${m(0)(0)},${m(1)(0)},${m(2)(0)},${m(2)(1)},${m(2)(2)}"
}

object ShortTupleFlipper extends TupleFlipper {

  override def toMatrix(value: String): M = {
    val s: Array[String] = value.replaceAll("[()]", "").split(",")
    M(Array(s(2), "0", s(1)), Array(s(3), "0", s(0)), Array(s(4), "0", s(5)))
  }
  override def toTuple(m: M) = s"${m(1)(2)},${m(0)(2)},${m(0)(0)},${m(1)(0)},${m(2)(0)},${m(2)(2)}"
}
