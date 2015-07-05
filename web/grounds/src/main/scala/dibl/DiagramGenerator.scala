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

import org.scalajs.dom.raw.{Node, Attr, Console, Document}

import scala.util.Try

/** @param newLabels content of this map by example:
  *                  "A1" -> "tc (-1,0,1,0,1,0,-1,0)",
  *                  "A2" -> "tc (1,0,1,0,-1,0,-1,0)",
  *                  "A3" -> "tctc (1,0,0,0,1,-1,0,-1)",
  *                  "B1" -> "tc (0,1,1,0,0,0,-1,-1)",
  *                  "B2" -> "tc (0,0,1,1,-1,-1,0)",
  *                  "B3" -> "tctc (-1,1,0,1,-1,0,0,0)"
  *                  See also [[Settings.stitches]]
  */
class DiagramGenerator(newLabels: Map[String, String])(implicit console: Console, debug: Boolean) {

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
  def apply(document: Document) = {

    if (debug) console.info(s"NEW_LABLES = $newLabels")

    val stitches: Map[String, String] =
      (for { node <- document.getNodes(tag="g", parentLabel="pile") } yield {
        (node.inkscapeLabelOrElse("LLL"), node.idOrElse("IIII"))
      }).toMap

    if (debug) console.info(s"STITCHES = $stitches<br> ")

    for { node <- document.getNodes(tag="use", parentLabel="base tile") } {
      val key = node.inkscapeLabelOrElse("KKK")
      val newLabel = newLabels.getOrElse(key, "LLL")
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
