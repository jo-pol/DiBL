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

import org.scalajs.dom.raw._
import scala.scalajs.js.annotation.JSExport
import scala.util.Try

@JSExport
object Ground {

  val debug = false

  @JSExport
  def main(window: Window): Unit = {

    implicit val console = window.console
    val htmlDoc = window.document 
    val uri = htmlDoc.documentURI // trouble for IE
    
    if (debug) console.info(s"Analysing arguments: $uri")
    val s = Settings(uri)
    if (debug) console.info(s.toString)
    val templateUrl: String = s"templates/${s.template}.svg"
    htmlDoc.getElementById("message").innerHTML += s"$s<br><br>loading $templateUrl "

    import org.scalajs.dom.ext._
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
    Ajax.get(templateUrl).onSuccess{ case xhr =>

      val svgDoc = new DOMParser().parseFromString(xhr.responseText, "image/svg+xml")
      replaceStitches(svgDoc, s.stitches)
      try {
        // firefox/chrome now can save the generated diagram
        htmlDoc.body.outerHTML = svgDoc.documentElement.outerHTML
      } catch {
        case e: Throwable =>
          // fallback for safari, which only can save the original template
          htmlDoc.write(xhr.responseText)
          replaceStitches(htmlDoc, s.stitches)
          htmlDoc.close()
      }
    }
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
    * For each cell (A1-..) in the base tile, look up the newLabel in the pile.
    * This results in an ID. This ID becomes the new value of the href attribute in the base tile.
    */
  def replaceStitches(document:Document, newLabels: Map[String,String])(implicit console: Console) = {

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
