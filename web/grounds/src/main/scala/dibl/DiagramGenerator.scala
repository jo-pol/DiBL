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

import org.scalajs.dom.raw.{Node, Attr, Console, Document}

import scala.scalajs.js.annotation.JSExport
import scala.util.Try

@JSExport
class DiagramGenerator(s: Settings)(implicit console: Console, debug: Boolean) {

  @JSExport
  val templateURI: String = s"http://jo-pol.github.io/DiBL/grounds/templates/${s.template}.svg"


  /** Replaces stitches in an SVG template.
    *
    * For each cell (A1-..) in the base tile, look up the newLabel in the pile.
    * This results in an ID. This ID becomes the new value of the href attribute in the base tile.
    *
    * @param document crucial content by a summarized example:
    *                 {{{
    *                 <g inkscape:label="base tile">
    *                 <use inkscape:label="A1" xlink:href="#g123"></use>
    *                 <use inkscape:label="A2" xlink:href="#g456"></use>
    *                 ...
    *                 </g>
    *                 <g inkscape:label="pile">
    *                 <g id="g123" inkscape:label="tc (0,1,1,0,-1,-1)">...</g>
    *                 <g id="g456" inkscape:label="tc (1,0,1,0,-1,-1)">...</g>
    *                 ...
    *                 </g>
    *                 }
    */
  @JSExport
  def apply(document: Document): Unit = {

    if (debug) console.info(s"NEW_LABLES = ${s.stitches}")

    val stitches: Map[String, String] =
      (for { node <- document.getNodes(tag="g", parentLabel="pile") } yield {
        (node.inkscapeLabelOrElse("LLL"), node.idOrElse("IIII"))
      }).toMap

    if (debug) console.info(s"STITCHES = $stitches<br> ")

    for { node <- document.getNodes(tag="use", parentLabel="base tile") } {
      val key = node.inkscapeLabelOrElse("KKK")
      val newLabel = s.stitches.getOrElse(key, "LLL")
      val newHref =  s"#${stitches.getOrElse(newLabel,"NNN")}"
      val href = Try(node.attributes.getNamedItem("xlink:href")).getOrElse(new Attr())
      if (debug) console.info(s"[$key -> $newLabel - ${href.value} -> $newHref] ")
      if (!newHref.equals("#NNN"))
        href.value = newHref
    }
  }

  implicit class DocumentExtension(val left: Document) {

    def getNodes(tag: String, parentLabel: String): IndexedSeq[Node] = {
      val list = left.getElementsByTagName(tag)
      for {i <- 0 until list.length
           if list.item(i).parentNode.inkscapeLabelOrElse("") == parentLabel
      } yield {
        list.item(i)
      }
    }
  }

  implicit class NodeExtension(val left: Node) {

    def inkscapeLabelOrElse(default: String): String =
      Try(left.attributes.getNamedItem("inkscape:label").value).getOrElse(default)

    def idOrElse(default: String): String =
      Try(left.attributes.getNamedItem("id").value).getOrElse(default)
  }

}
