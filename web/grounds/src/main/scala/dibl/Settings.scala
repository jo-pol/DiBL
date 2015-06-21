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
package dibl

import scala.util.Try

/** interprets uri query: "key1=value1&key2=value2&..." */
case class Settings(uri: String) {

  private val queryMap: Map[String, String] = {
    (for {s <- uri.replaceAll("[^?]+[?]", "").split("&")}
      yield (s.replaceAll("=.*", ""), s.replaceAll("^[^=]+=*", ""))
    ).toMap
  }

  /** The base name (no path, no extension) of an SVG document */
  val template: String = Try(queryMap("template")).getOrElse( "diagonal-3x3-thread")

  // TODO flip matrices: https://github.com/jo-pol/DiBL/tree/master/standalone/tiles/dibl-tiles/src/main/java/dibl/math
  // see also http://stackoverflow.com/questions/10456918/scala-matrix-library-to-calculate-large-fibonacci-numbers

  /** A map of use element labels ("A1"-"C3" for a 3x3 matrix) in the group labeled "base tile"
    * to group element labels (e.g. "tc (0,1,1,0,-1,-1)") in the group labeled "pile".
    *
    * The permutations of "tc" imply the twist/cross actions composing a stitch.
    * A stitch is made with four threads at the nodes of a two-in two-out directed graph.
    * The tuple specifies the orientation of the four 'legs' of a stitch, e.g. like: |<, >|, ><, _V_
    * See also <a href="https://github.com/jo-pol/DiBL/wiki/Input-Files">matrices and tuples</a>.
    * */
  val stitches: Map[String, String] = {

    val tupleMatrix: M = {
      val fallBack = Array(M(R("","","",""),R("","","",""),R("","","",""),R("","","","")))
      val matrixKey: String = template.replace("-thread", "").replace("-pair", "")
      val matrices: Array[M] = Matrices.matrixMap.getOrElse(matrixKey, fallBack)
      val pattern: Int = Try(queryMap("pattern").toInt).getOrElse(0)
      matrices(math.min(matrices.length-1,math.max(0,pattern)))
    }
    /** extract P,Q from "aaa-PxQ-bbb", 2<=P<=4, 2<=Q<=4 */
    val dimensions: Array[Int] = 
      for {s <- {
        val strings1 = template.split("-")
        val strings2 = if (strings1.length<2) Array("2","2") else strings1(1).split("x")
        if (strings2.length<2) Array("2","2") else strings2
      }} 
        yield math.min(4,math.max(2,Try(s.toInt).getOrElse(2)))

    (for {
      r <- 0 until dimensions(0)
      c <- 0 until dimensions(1)
    } yield {
      val key: String = s"${"ABCDE".substring(c, c+1)}${r+1}"
      val stitch: String = queryMap.getOrElse(key,"tc")
      val tuple: String = Try(tupleMatrix(r)(c)).getOrElse("")
      (key, s"$stitch ($tuple)")
    }).toArray.toMap
  }
  override def toString: String = s"$queryMap TEMPLATE: $template STITCHES: $stitches"
}
