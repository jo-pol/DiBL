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

@JSExport
object Ground {
  type Tuple = String
  type Matrix = Array[Array[String]]
  type Matrices = Array[Matrix]
  type MatrixMap = mutable.HashMap[String, Matrices]

  val xpathStitches = "//g[@inkscape:label='pile']/g[@inkscape:label]"
  val xpathCells = "//g[@inkscape:label='base tile']/use[@inkscape:label]"

  @JSExport
  def main(document: Document): Unit = {
    document.getElementById("demo").innerHTML += " it's me "
    val s = Settings (document.documentURI)
    val template: String = s.getArg("template","diagonal-3x3-thread")
    val pattern: Int = s.getArg("pattern","0").toInt
    val matrixKey: String = template.replace("-thread","").replace("-pair","")
    //val matrix: Matrix = matricesOfTuples.get(matrixKey).get(pattern)

    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET", s"http://jo-pol.github.io/DiBL/grounds/templates/${template}.svg")
    xhr.onload = (e: dom.Event) => {
        if (xhr.status == 200) {
          document.write( replaceStitches(xhr.responseText, s) )
        }
    }
    xhr.send()
  }
  def replaceStitches(svg:String, s: Settings): String = {
      //val stitches =...foreach yield
      //{ node => (node.attribute("inkscape:label") -> node.attribute("id")) }
      //val cells =...foreach yield
      //{ node => (node.attribute("inkscape:label") -> node) }
      svg
  }
}
