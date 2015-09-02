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

import scala.collection.immutable.HashMap
import scala.collection.Map
import org.scalatest._

class ConvertSpec extends FlatSpec with Matchers {

  "conversion" should "do something" in {
    val m = Graphs.get("brick-4x4-pair",0,M(R("")))
    println(s"${m.deep}")
    pack( m ) should be ("=:0g:::0?uuu=<:g")
  }

  def fromTuple = Map() ++ toTuple.map(_.swap)
  def pack(m: M): String = {
    var s = ""
    for {
      r <- m.indices
      c <- m(0).indices
    } s = s + fromTuple.getOrElse(m(r)(c),"?")
    s
  }

  private val toTuple = HashMap(
    " " -> "0,0,0,0,0,0,0,0",
    "0" -> "-1,0,0,1,1,-1,0,0",
    "1" -> "-1,0,0,1,1,0,-1,0",
    "2" -> "-1,0,0,1,1,0,0,-1",
    "3" -> "-1,0,1,0,1,-1,0,0",
    "4" -> "-1,0,1,0,1,0,-1,0",
    "5" -> "-1,0,1,0,1,0,0,-1",
    "6" -> "-1,0,1,1,-1,0,0,0",
    "7" -> "-1,0,1,1,0,-1,0,0",
    "8" -> "-1,0,1,1,0,0,-1,0",
    "9" -> "-1,0,1,1,0,0,0,-1",
    ":" -> "-1,1,0,0,1,-1,0,0",
    ";" -> "-1,1,0,0,1,0,-1,0",
    "<" -> "-1,1,0,0,1,0,0,-1",
    "=" -> "-1,1,0,1,-1,0,0,0",
    ">" -> "-1,1,0,1,0,-1,0,0",
    "?" -> "-1,1,0,1,0,0,-1,0",
    "@" -> "-1,1,0,1,0,0,0,-1",
    "A" -> "-1,1,1,0,-1,0,0,0",
    "B" -> "-1,1,1,0,0,-1,0,0",
    "C" -> "-1,1,1,0,0,0,-1,0",
    "D" -> "-1,1,1,0,0,0,0,-1",
    "E" -> "0,0,0,1,1,-1,-1,0",
    "F" -> "0,0,0,1,1,-1,0,-1",
    "G" -> "0,0,0,1,1,0,-1,-1",
    "H" -> "0,0,1,0,1,-1,-1,0",
    "I" -> "0,0,1,0,1,-1,0,-1",
    "J" -> "0,0,1,0,1,0,-1,-1",
    "K" -> "0,0,1,1,-1,-1,0,0",
    "L" -> "0,0,1,1,-1,0,-1,0",
    "M" -> "0,0,1,1,-1,0,0,-1",
    "N" -> "0,0,1,1,0,-1,-1,0",
    "O" -> "0,0,1,1,0,-1,0,-1",
    "P" -> "0,0,1,1,0,0,-1,-1",
    "Q" -> "0,1,0,0,1,-1,-1,0",
    "R" -> "0,1,0,0,1,-1,0,-1",
    "S" -> "0,1,0,0,1,0,-1,-1",
    "T" -> "0,1,0,1,-1,-1,0,0",
    "U" -> "0,1,0,1,-1,0,-1,0",
    "V" -> "0,1,0,1,-1,0,0,-1",
    "W" -> "0,1,0,1,0,-1,-1,0",
    "X" -> "0,1,0,1,0,-1,0,-1",
    "Y" -> "0,1,0,1,0,0,-1,-1",
    "Z" -> "0,1,1,0,-1,-1,0,0",
    "a" -> "0,1,1,0,-1,0,-1,0",
    "b" -> "0,1,1,0,-1,0,0,-1",
    "c" -> "0,1,1,0,0,-1,-1,0",
    "d" -> "0,1,1,0,0,-1,0,-1",
    "e" -> "0,1,1,0,0,0,-1,-1",
    "f" -> "1,0,0,0,1,-1,-1,0",
    "g" -> "1,0,0,0,1,-1,0,-1",
    "h" -> "1,0,0,0,1,0,-1,-1",
    "i" -> "1,0,0,1,-1,-1,0,0",
    "j" -> "1,0,0,1,-1,0,-1,0",
    "k" -> "1,0,0,1,-1,0,0,-1",
    "l" -> "1,0,0,1,0,-1,-1,0",
    "m" -> "1,0,0,1,0,-1,0,-1",
    "n" -> "1,0,0,1,0,0,-1,-1",
    "o" -> "1,0,1,0,-1,-1,0,0",
    "p" -> "1,0,1,0,-1,0,-1,0",
    "q" -> "1,0,1,0,-1,0,0,-1",
    "r" -> "1,0,1,0,0,-1,-1,0",
    "s" -> "1,0,1,0,0,-1,0,-1",
    "t" -> "1,0,1,0,0,0,-1,-1",
    "u" -> "1,1,0,0,-1,-1,0,0",
    "v" -> "1,1,0,0,-1,0,-1,0",
    "w" -> "1,1,0,0,-1,0,0,-1",
    "w" -> "1,1,0,0,0,-1,-1,0",
    "y" -> "1,1,0,0,0,-1,0,-1",
    "z" -> "1,1,0,0,0,0,-1,-1")
  
}
