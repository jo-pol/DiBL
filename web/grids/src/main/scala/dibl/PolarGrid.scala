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
import org.scalajs.dom.html
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all.{canvas}

@JSExport
object PolarGrid {
  @JSExport
  def main(target: html.Div, uri: String): Unit = {

    val s = Settings(uri)
    val canvasContext = {
      while (target.hasChildNodes())
        target.removeChild(target.children.item(0))
      val gridCanvas = canvas().render
      target.appendChild(gridCanvas)
      gridCanvas.height = s.canvasSize
      gridCanvas.width = s.canvasSize
      gridCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    }
    def plotArc(p: Point, dotRadius: Double) = {
      canvasContext.beginPath()
      canvasContext.arc(p.x, p.y, dotRadius, 0, 2 * Math.PI, anticlockwise = false)
      canvasContext
    }
    def plotRingOfDots(radius: Double, ringNr: Int) = {
      for (dotNr <- 0 until s.dotsPerRing) {
        val point = Point(radius, dotNr, ringNr, s)
        val plotType = s.plotType(point)
        if (plotType == '1') plotArc(point, 1).fill()
        else if (plotType == '2') plotArc(point, point.arcLength / 2.0).stroke()
      }
    }
    val change = {
      /** see https://github.com/jo-pol/DiBL/pull/5/files#diff-326bb6c119212f45e228f5e0516187e3R19 */
      val correction = Math.tan(s.angle * 0.93) * Math.PI / (4 * s.dotsPerRing)
      Math.tan(s.angle - correction) * Math.PI / s.dotsPerRing
    }
    var radius = s.outerRadius
    var ringNr = 0
    while (radius > s.innerRadius && radius * change > 2 && Point(radius, 0, ringNr, s).arcLength > 2) {
      plotRingOfDots(radius, ringNr)
      radius -= radius * change
      ringNr += 1
    }
    for ((k,v) <- s.queryMap)
      try{
        target.ownerDocument.getElementById(k).setAttribute("value",v(0))
      }catch {case _: Throwable => }
  }
}
