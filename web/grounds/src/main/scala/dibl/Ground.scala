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

import org.scalajs.dom.raw._
import scala.scalajs.js.annotation.JSExport

@JSExport
object Ground {

  implicit val debug = false

  @JSExport
  def main(window: Window, uri: String): Unit = {

    implicit val console = window.console
    val htmlDoc: HTMLDocument = window.document

    if (debug) console.info(s"Analysing arguments: $uri")
    implicit val s:Settings = Settings.parseUri(uri)
    if (debug) console.info(s.toString)
    val templateUrl: String = s"http://jo-pol.github.io/DiBL/grounds/templates/${s.template}.svg"
    htmlDoc.getElementById("message").innerHTML += s"$s<br><br>loading $templateUrl "

    import org.scalajs.dom.ext._

    import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
    Ajax.get(templateUrl).onSuccess{
      case xhr => generateDiagram (xhr.responseText, htmlDoc)
    }
  }

  def generateDiagram (svgString: String, htmlDoc: org.scalajs.dom.html.Document)(implicit console: Console, s: Settings) = {

      val diagramGenerator = new DiagramGenerator(s.stitches)
      val svgDoc = new DOMParser().parseFromString(svgString, "image/svg+xml")
      if ( !scala.scalajs.js.isUndefined(svgDoc.documentElement)
        && !scala.scalajs.js.isUndefined(svgDoc.documentElement.outerHTML)
        && !scala.scalajs.js.isUndefined(htmlDoc.body)
        && !scala.scalajs.js.isUndefined(htmlDoc.body.innerHTML)) {
        // FF/Chrome can save the generated diagram
        diagramGenerator.apply(svgDoc)
        htmlDoc.body.innerHTML = svgDoc.documentElement.outerHTML
      } else {
        // fall back for safari/IE,  they only can save the original template
        htmlDoc.write(svgString)
        // IE now throws "access denied" when trying to log:
        diagramGenerator.apply(htmlDoc)
        htmlDoc.close()
      }
  }

}
