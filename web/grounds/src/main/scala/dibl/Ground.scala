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
import org.scalajs.dom.raw._
import scala.scalajs.concurrent.JSExecutionContext.Implicits
import scala.scalajs.js.annotation.JSExport
import scala.util.Try

@JSExport
object Ground {

  @JSExport
  def main(document: Document): Unit = {

    val msg: Element = document.getElementById("message")
    msg.innerHTML += s"<br>Analysing arguments: ${document.documentURI}"
    val s = Settings(document.documentURI)
    val templateUrl: String = s"http://jo-pol.github.io/DiBL/grounds/templates/${s.template}.svg"
    msg.innerHTML += s"<br><br>$s<br><br>loading $templateUrl "

    import dom.ext._
    import Implicits.runNow

    Ajax.get(templateUrl).onSuccess{ case xhr =>
      msg.innerHTML += " replacing stitches... "
      // TODO try: window.openWindow(relativeUrl,"bobbin-lace-diagram")
      document.write(xhr.responseText)
      replaceStitches(document, s.stitches)
      // TODO stop the busy icon of the browser
      // return / xhr.abort don't fix it
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
    * For each cell (A1-..) in the base tile, look up the new label in the pile.
    * This results in an ID. This ID becomes the new value of the href attribute in the base tile.
    */
  def replaceStitches(doc:Document, newLabels: Map[String,String]) = {

    //doc.write (s"<br><br>${doc.documentURI}<br><br>$newLabels")

    val stitches: Map[String, String] = (for {
      node <- doc.getNodesByTag("g")
      if node.parentNode.inkscapeLabelOrElse("") == "pile"
    } yield {
        (node.inkscapeLabelOrElse("LLL"), node.idOrElse("IIII"))
      }).toMap
    //doc.write (s"<br><br>$stitches<br> ")
    for {
      node <- doc.getNodesByTag("use")
      if node.parentNode.inkscapeLabelOrElse("") == "base tile"
    } {
      val key = node.inkscapeLabelOrElse("KKK")
      val newLabel = newLabels.getOrElse(key, "LLL")
      val newHref =  s"#${stitches.getOrElse(newLabel,"NNN")}"
      val href = Try(node.attributes.getNamedItem("xlink:href")).getOrElse(new Attr())
      //doc.write (s"<br>[$key $newLabel ${href.value} $newHref] ")
      href.value = newHref
    }
  }
}
