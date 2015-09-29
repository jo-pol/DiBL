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

import scala.collection.Map

class ConvertSpec extends FlatSpec with Matchers {

  "brick-4x4" should "converts back and forth" in {
    val m = Graphs.get("brick-4x4-pair",0,M(R("")))
    pack( m ) should be ("=:0g:::0wuuu=<:g")
    Graphs.unpack( pack( m ),4,4 ) should equal (m)
  }

  "flanders-3x2" should "convert back and forth" in {
    val m: M = Graphs.get("flanders-3x2-pair",0,M(R("")))
    pack(m) should be ("4epNg=")
    Graphs.unpack(pack(m), m.length, m(0).length ) should equal (m)
  }

  "convert" should "produce packed matrices for further development" in {
    for ((key,value)<-Graphs.matrixMap) {
      // TODO transform short tuples to long ones for diagonal
      print(s"$key={")
      for (m <- value)
        print('"' + pack(m)+'"'+',')
      println(s"}") // "${pack(m)}"
    }
  }

  def fromTuple = Map() ++ Graphs.toTuple.map(_.swap)
  def pack(m: M): String = {
    var s = ""
    for {
      r <- m.indices
      c <- m(0).indices
    } s = s + fromTuple.getOrElse(m(r)(c),"?")
    s
  }
}
