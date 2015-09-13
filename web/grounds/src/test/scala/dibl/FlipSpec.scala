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

import org.scalatest._
import dibl.MatrixFlipper._
import dibl.Graphs._

class FlipSpec extends FlatSpec with Matchers {

  val m3x3 = M(R("1", "2", "3"), 
               R("4", "5", "6"), 
               R("7", "8", "9"))
  val m4x4 = M(R("A", "B", "C", "D"), 
               R("E", "F", "G", "H"), 
               R("I", "J", "K", "L"), 
               R("M", "N", "O", "P"))
  val m4x2 = M(R("A", "B"), 
               R("C", "D"), 
               R("E", "F"), 
               R("G", "H"))
  val m2x4 = M(R("A", "B", "C", "D"), 
               R("E", "F", "G", "H"))
               
  val brickFlipper = new BrickFlipper(new Flipper[String] {
    def flipLeftRight(o: String): String = o
    def flipBottomUp(o: String): String = o
    def flipNW2SE(o: String): String = o
    def flipNE2SW(o: String): String = o
  })
  
  "y 3x3" should "succeed" in {
    brickFlipper.flipLeftRight(fromBrickToCheckerboard(m3x3)) shouldBe
      M(R("3", "2", "1"), 
        R("5", "4", "6"), 
        R("9", "8", "7"))
  }
  "x 3x3" should "succeed" in {
    brickFlipper.flipBottomUp(fromBrickToCheckerboard(m3x3)) shouldBe 
      M(R("7", "8", "9"), 
        R("4", "5", "6"), 
        R("1", "2", "3"))
  }
  "y 4x4" should "succeed" in {
    brickFlipper.flipLeftRight(fromBrickToCheckerboard(m4x4)) shouldBe
      M(R("D", "C", "B", "A"),
        R("G", "F", "E", "H"), 
        R("L", "K", "J", "I"), 
        R("O", "N", "M", "P"))
  }
  "x 4x4" should "succeed" in {
    brickFlipper.flipBottomUp(fromBrickToCheckerboard(m4x4)) shouldBe
      M(R("M", "N", "O", "P"),
        R("J", "K", "L", "I"),
        R("E", "F", "G", "H"), 
        R("B", "C", "D", "A"))
  }
  
  "flip back and forth" should "render original" in {
    val m1 = Graphs.get("interleaved-4x2-pair",0,M(R("")))
    val flipped: M = flipInterleaved("x", m1)
    val m2 = flipInterleaved("x",flipped)
    for {
      r <- m1.indices
      c <- m1(0).indices
    } m1(r)(c) should equal (m2(r)(c))
  }
}
