package dibl

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

import org.scalajs.dom
import org.scalajs.dom.html.Document

import scala.collection.immutable.HashMap
import scala.scalajs.js.annotation.JSExport

@JSExport
object Ground {

  @JSExport
  def main(document: Document): Unit = {
    val s = Settings(document.documentURI)
    document.getElementById("message").innerHTML += s" $s loading diagram... "

    // TODO perhaps replace with https://lihaoyi.github.io/hands-on-scala-js/#dom.extensions
    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET", s"http://jo-pol.github.io/DiBL/grounds/templates/${s.template}.svg")
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200) {
        document.getElementById("message").innerHTML += " replacing stitches... "
        //document.write(replaceStitches(xhr.responseText, s.stitches))
        // TODO stop the busy icon of the browser
      }
    }
    xhr.send()
  }

  /** Replaces stitches in an SVG template.
    *
    * An extract of a template:
    * {{{
    *    <g inkscape:label="base tile">
    *      <use inkscape:label="A1" xlink:href="#g123"></use>
    *      <use inkscape:label="A2" xlink:href="#g456"></use>
    *      ...
    *    </g>
    *    <g inkscape:label="pile">
    *      <g id="g123" inkscape:label="tc (0,1,1,0,-1,-1)">...</g>
    *      <g id="g456" inkscape:label="tc (1,0,1,0,-1,-1)">...</g>
    *      ...
    *    </g>
    * }}}
    * For each cell (A1-..) in the base tile, look up the new label in the pile.
    * This results in an ID. This ID becomes the new values of the href attribute in the base tile. 
    */
  def replaceStitches(svg:String, newLabels: Map[String,String]): String = {

    val xpathStitches = "//g[@inkscape:label='pile']/g[@inkscape:label]"
    val xpathCells = "//g[@inkscape:label='base tile']/use[@inkscape:label]"

    // TODO fix linking errors or use something else than:
    // libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.2"

    // scala.xml.XML.loadString(svg)

    // val stitches =...foreach yield
    // { node => (node.attribute("inkscape:label") -> node.attribute("id")) }
    // for each cell:
    //   newLabel = newLabels.get(node.attribute("inkscape:label"))
    //   node.attribute("xlink:href") = "#" + stitches.get(newLabel)
    svg
  }
}
