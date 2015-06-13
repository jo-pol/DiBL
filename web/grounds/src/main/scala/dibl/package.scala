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
 along with this program. If not, see http://www.gnu.org/licenses/.package dibl
  */

import scala.collection.immutable.HashMap

package object dibl {
  private val matrixMap = {
    val R = Array // row
    val M = Array // matrix
    HashMap(
      "diagonal-3x3" -> Array(
        M(R("0,1,1,0,-1,-1", "1,0,1,0,-1,-1", "0,0,1,1,-1,-1"), R("0,1,1,-1,-1,0", "-1,1,0,1,0,-1", "0,1,1,0,-1,-1"), R("-1,1,1,0,-1,0", "0,0,0,0,0,0", "1,1,0,-1,0,-1")),
        M(R("1,0,1,0,-1,-1", "1,0,1,0,-1,-1", "0,0,1,1,-1,-1"), R("0,1,1,-1,-1,0", "-1,1,0,1,0,-1", "0,1,1,-1,0,-1"), R("-1,1,1,0,0,-1", "1,0,1,-1,0,-1", "1,0,1,-1,0,-1")),
        M(R("1,0,1,0,-1,-1", "0,0,1,1,-1,-1", "1,0,1,0,-1,-1"), R("-1,1,0,1,-1,0", "0,1,0,1,-1,-1", "0,1,1,-1,-1,0"), R("-1,1,1,-1,0,0", "1,1,0,-1,0,-1", "-1,1,1,0,0,-1")),
        M(R("1,0,1,0,-1,-1", "1,0,1,-1,0,-1", "0,0,1,1,-1,-1"), R("1,1,0,0,-1,-1", "-1,0,1,1,0,-1", "0,1,1,-1,-1,0"), R("-1,1,1,0,0,-1", "1,0,1,-1,0,-1", "0,1,1,-1,0,-1")),
        M(R("0,0,1,1,-1,-1", "-1,1,1,-1,0,0", "1,1,0,0,-1,-1"), R("1,1,0,0,-1,-1", "0,0,1,1,-1,-1", "-1,1,1,-1,0,0"), R("-1,1,1,-1,0,0", "1,1,0,0,-1,-1", "0,0,1,1,-1,-1")),
        M(R("1,0,1,0,-1,-1", "-1,0,1,1,-1,0", "1,1,0,0,-1,-1"), R("-1,1,1,0,-1,0", "0,1,0,1,-1,-1", "0,1,1,-1,0,-1"), R("-1,1,1,-1,0,0", "1,1,0,-1,0,-1", "0,0,1,1,-1,-1")),
        M(R("1,0,1,0,-1,-1", "-1,1,1,0,0,-1", "1,0,1,0,-1,-1"), R("0,1,0,1,-1,-1", "0,0,1,1,-1,-1", "0,1,1,-1,-1,0"), R("-1,1,1,-1,0,0", "1,1,0,-1,-1,0", "-1,1,0,1,0,-1")))
      , "brick-3x3" -> Array(
        M(R("-1,1,0,1,-1,0,0,0", "0,0,0,1,1,-1,0,-1", "1,1,0,0,0,-1,0,-1"), R("1,1,0,0,0,-1,0,-1", "-1,1,0,1,-1,0,0,0", "0,0,0,1,1,-1,0,-1"), R("-1,1,0,1,-1,0,0,0", "0,0,0,1,1,-1,0,-1", "1,1,0,0,0,-1,0,-1")),
        M(R("-1,1,0,1,-1,0,0,0", "0,1,0,0,1,-1,0,-1", "1,1,0,0,0,-1,0,-1"), R("1,1,0,0,-1,0,0,-1", "0,1,0,1,-1,0,0,-1", "1,0,0,1,0,-1,0,-1"), R("-1,0,0,1,1,-1,0,0", "0,0,0,1,1,-1,0,-1", "-1,1,0,1,0,-1,0,0")),
        M(R("-1,1,0,1,-1,0,0,0", "0,1,0,0,1,-1,0,-1", "1,1,0,0,0,-1,0,-1"), R("0,1,0,0,1,-1,0,-1", "-1,1,0,1,0,0,0,-1", "-1,0,0,1,1,0,0,-1"), R("0,1,0,1,-1,-1,0,0", "1,0,0,1,0,-1,0,-1", "1,0,0,1,-1,-1,0,0")),
        M(R("-1,1,0,1,0,-1,0,0", "-1,1,0,0,1,-1,0,0", "0,1,0,0,1,-1,0,-1"), R("-1,1,0,0,1,-1,0,0", "0,1,0,0,1,-1,0,-1", "-1,1,0,1,0,0,0,-1"), R("0,1,0,1,-1,-1,0,0", "1,1,0,0,0,-1,0,-1", "1,0,0,1,-1,-1,0,0")),
        M(R("-1,1,0,1,-1,0,0,0", "0,1,0,0,1,-1,0,-1", "1,0,0,1,0,-1,0,-1"), R("1,1,0,0,-1,0,0,-1", "0,1,0,1,-1,-1,0,0", "1,0,0,1,0,-1,0,-1"), R("0,0,0,1,1,-1,0,-1", "-1,1,0,1,0,0,0,-1", "-1,1,0,0,1,-1,0,0")),
        M(R("-1,1,0,1,0,-1,0,0", "-1,1,0,0,1,-1,0,0", "0,0,0,1,1,-1,0,-1"), R("1,1,0,0,0,-1,0,-1", "1,1,0,0,-1,-1,0,0", "0,1,0,1,-1,-1,0,0"), R("0,1,0,0,1,-1,0,-1", "-1,1,0,1,0,0,0,-1", "-1,1,0,0,1,-1,0,0")),
        M(R("-1,1,0,1,0,-1,0,0", "-1,1,0,0,1,0,0,-1", "0,0,0,1,1,-1,0,-1"), R("0,0,0,0,0,0,0,0", "0,1,0,1,0,-1,0,-1", "0,1,0,1,0,-1,0,-1"), R("1,0,0,1,0,-1,0,-1", "1,1,0,0,-1,0,0,-1", "0,1,0,1,-1,-1,0,0")),
        M(R("0,1,0,1,0,-1,0,-1", "0,1,0,1,0,-1,0,-1", "0,1,0,1,0,-1,0,-1"), R("0,1,0,1,0,-1,0,-1", "0,1,0,1,0,-1,0,-1", "0,1,0,1,0,-1,0,-1"), R("0,1,0,1,0,-1,0,-1", "0,1,0,1,0,-1,0,-1", "0,1,0,1,0,-1,0,-1")))
    )
  }

  /** See <a href="https://github.com/jo-pol/DiBL/wiki/Input-Files">matrices and tuples</a> */
  def getTupleMatrix(template: String, pattern: Int): Array[Array[String]] =
    matrixMap.get(template.replace("-thread", "").replace("-pair", "")).get(pattern)
}
