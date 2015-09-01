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

class FlipSpec extends FlatSpec with Matchers {
  "repeated flip along x" should "render original" in {
    val m1 = Graphs.get("interleaved-4x4-pair",0,M(R("")))
    val m2 = MatrixFlipper.flipInterleaved("x",MatrixFlipper.flipInterleaved("x",m1))
    for {
      r <- m1.indices
      c <- m1(0).indices
    } m1(r)(c) should equal (m2(r)(c))
  }
}
