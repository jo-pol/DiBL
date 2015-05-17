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

import org.scalajs.dom.html
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object PolarGrid {
  @JSExport
  def main(target: html.Div): Unit = {

    // https://developer.mozilla.org/en-US/docs/Web/API/HTMLCanvasElement/toDataURL
    val scale = 96 / 25.4 // DPI / mm

    // parse uri query: "key1=value1&key2=value2&..."
    val params = target.ownerDocument.documentURI.toString.replaceAll("^[^?]+.", "")
    val m: Array[List[String]] = for {s <- params.split("&")} yield s.split("=").toList
    //TODO convert to Map[String, String] to extract the next values, don't bother duplicates
    val outerDiameter = 150.0 * scale
    val innerDiameter = 100.0 * scale
    val dotsPerCircle = 150
    val angle = 30

    val alpha = Math.toRadians(360.0 / dotsPerCircle.toDouble)
    val change = {
      val beta = Math.toRadians(angle)
      val correction = Math.tan(beta * 0.93) * Math.PI / (4 * dotsPerCircle)
      Math.tan(beta - correction) * Math.PI / dotsPerCircle
    }
    val canvasContext = {
      val size = Math.round(outerDiameter + 2.0).toInt
      val gridCanvas = canvas().render
      while (target.hasChildNodes())
        target.removeChild(target.children.item(0))
      target.appendChild(p(params.replaceAll("&", " ")).render)
      target.appendChild(gridCanvas)
      gridCanvas.height = size
      gridCanvas.width = size
      gridCanvas.getContext("2d")
    }
    def plotDot(x: Double, y: Double) = {
      canvasContext.beginPath()
      canvasContext.arc(x, y, 1, 0, 2 * Math.PI, false)
      canvasContext.fill()
    }
    def plotCircle(radius: Double, offset: Double) = {
      for (i <- 0 until dotsPerCircle) {
        val a = (i.toDouble + offset) * alpha
        val x = radius * Math.cos(a)
        val y = radius * Math.sin(a)
        plotDot(x + outerDiameter / 2 + 1, y + outerDiameter / 2 + 1)
      }
    }

    var diameter = outerDiameter
    var offset = 0.5
    while (diameter > innerDiameter) {
      plotCircle(diameter / 2, offset)
      diameter -= diameter * change
      offset = Math.abs(offset - 0.5)
    }
  }
}
