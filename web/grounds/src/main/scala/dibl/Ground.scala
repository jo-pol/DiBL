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

import org.scalajs.dom.html._
import org.scalajs.dom.html.Document
import scala.collection.mutable
import scalajs.js.annotation.JSExport
import scala.xml.PrettyPrinter

@JSExport
object Ground {
  type Tuple = String
  type Matrix = Array[Array[String]]
  type Matrices = Array[Matrix]
  type MatrixMap = mutable.HashMap[String, Matrices]

  @JSExport
  def main(document: Document, matricesOfTuples: MatrixMap): Unit = {

    val s = Settings (document.documentURI)
    val template: String = s.getArg("template","flanders.svg")
    val pattern: Int = s.getArg("pattern","0").toInt
    val matrixKey: String = template.replace("-thread","").replace("-pair","").replace(".svg","")
    val matrix: Matrix = matricesOfTuples.get(matrixKey)(pattern)

    //Ajax.get(template)
    val svg = new PrettyPrinter(160, 2).format(
      <svg>
        <g>
          <g inkscape:label="base tile">
            <use inkscape:label="A1" xlink:href="#u1"></use>
            <use inkscape:label="A2" xlink:href="#u2"></use>
          </g>
          <g inkscape:label="pile">
            <g id="u1" inkscape:label="tc (0,1,1,0,-1,-1)"></g>
            <g id="u2" inkscape:label="tc (0,1,1,0,-1,-1)"></g>
          </g>
        </g>
      </svg>
    )

    val xpathStitches = "//g[@inkscape:label='pile']/g[@inkscape:label]"
    val stitches =...foreach yield
    { node => (node.attribute("inkscape:label") -> node.attribute("id")) }

    val xpathCells = "//g[@inkscape:label='base tile']/use[@inkscape:label]"
    val cells =...foreach yield
    { node => (node.attribute("inkscape:label") -> node) }
    ...

    document.write(svg)
  }
}
