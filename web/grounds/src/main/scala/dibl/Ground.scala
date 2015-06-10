package dibl

// Copyright 2015 Jo Pol
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see http://www.gnu.org/licenses/.package dibl

import java.net.URI

import org.scalajs.dom
import org.scalajs.dom.html.Document
import scala.scalajs.js.annotation.JSExport
import scala.collection.mutable
import collection.immutable.HashMap

@JSExport
object Ground {
  val R = Array
  val M = Array
  val tupleMatrices = Array( // diagonal 3x3
    M(R("0,1,1,0,-1,-1","1,0,1,0,-1,-1","0,0,1,1,-1,-1"),R("0,1,1,-1,-1,0","-1,1,0,1,0,-1","0,1,1,0,-1,-1"),R("-1,1,1,0,-1,0","0,0,0,0,0,0","1,1,0,-1,0,-1")),
    M(R("1,0,1,0,-1,-1","1,0,1,0,-1,-1","0,0,1,1,-1,-1"),R("0,1,1,-1,-1,0","-1,1,0,1,0,-1","0,1,1,-1,0,-1"),R("-1,1,1,0,0,-1","1,0,1,-1,0,-1","1,0,1,-1,0,-1")),
    M(R("1,0,1,0,-1,-1","0,0,1,1,-1,-1","1,0,1,0,-1,-1"),R("-1,1,0,1,-1,0","0,1,0,1,-1,-1","0,1,1,-1,-1,0"),R("-1,1,1,-1,0,0","1,1,0,-1,0,-1","-1,1,1,0,0,-1")),
    M(R("1,0,1,0,-1,-1","1,0,1,-1,0,-1","0,0,1,1,-1,-1"),R("1,1,0,0,-1,-1","-1,0,1,1,0,-1","0,1,1,-1,-1,0"),R("-1,1,1,0,0,-1","1,0,1,-1,0,-1","0,1,1,-1,0,-1")),
    M(R("0,0,1,1,-1,-1","-1,1,1,-1,0,0","1,1,0,0,-1,-1"),R("1,1,0,0,-1,-1","0,0,1,1,-1,-1","-1,1,1,-1,0,0"),R("-1,1,1,-1,0,0","1,1,0,0,-1,-1","0,0,1,1,-1,-1")),
    M(R("1,0,1,0,-1,-1","-1,0,1,1,-1,0","1,1,0,0,-1,-1"),R("-1,1,1,0,-1,0","0,1,0,1,-1,-1","0,1,1,-1,0,-1"),R("-1,1,1,-1,0,0","1,1,0,-1,0,-1","0,0,1,1,-1,-1")),
    M(R("1,0,1,0,-1,-1","-1,1,1,0,0,-1","1,0,1,0,-1,-1"),R("0,1,0,1,-1,-1","0,0,1,1,-1,-1","0,1,1,-1,-1,0"),R("-1,1,1,-1,0,0","1,1,0,-1,-1,0","-1,1,0,1,0,-1"))
  )

  val xpathStitches = "//g[@inkscape:label='pile']/g[@inkscape:label]"
  val xpathCells = "//g[@inkscape:label='base tile']/use[@inkscape:label]"

  @JSExport
  def main(document: Document): Unit = {
    val s = Settings (document.documentURI)
    
    // values from the HTML-form alias URI-query (relying on the defaults for now)
    val template: String = s.getArg("template","diagonal-3x3-thread")
    val pattern: Int = s.getArg("pattern","0").toInt
    val stitches = HashMap(
        "A1" -> "tc", "A2" -> "tc", "A3" -> "tc", 
        "B1" -> "tc", "B2" -> "tc", "B3" -> "tc", 
        "C1" -> "tc", "C2" -> "tc", "C3" -> "tc")

    val tuples: Array[Array[String]] = tupleMatrices(pattern)
    // TODO yield tuples onto stitches to produce labels: "tc (0,1,1,0,-1,-1)"
    val labels = stitches

    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET", s"http://jo-pol.github.io/DiBL/grounds/templates/${template}.svg")
    xhr.onload = (e: dom.Event) => {
        if (xhr.status == 200) {
          document.write( replaceStitches(xhr.responseText, labels) )
        }
    }
    xhr.send()
  }
  def replaceStitches(svg:String, labels: HashMap[String,String]): String = {
      //val stitches =...foreach yield
      //{ node => (node.attribute("inkscape:label") -> node.attribute("id")) }
      //val cells =...foreach yield
      //{ node => (node.attribute("inkscape:label") -> node) }
      svg
  }
}
