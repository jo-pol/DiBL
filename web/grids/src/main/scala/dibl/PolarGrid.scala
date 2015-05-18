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
import scalatags.JsDom.all.canvas

@JSExport
object PolarGrid {
  @JSExport
  def main(target: html.Div): Unit = {

    val s = Settings(new URI(target.ownerDocument.documentURI).getQuery)
    val canvasContext = {
      while (target.hasChildNodes())
        target.removeChild(target.children.item(0))
      val gridCanvas = canvas().render
      target.appendChild(s.toNode)
      target.appendChild(gridCanvas)
      gridCanvas.height = s.canvasSize
      gridCanvas.width = s.canvasSize
      gridCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    }
    def plotArc(p: Point, radius: Double) = {
      canvasContext.beginPath()
      canvasContext.arc(p.x, p.y, radius, 0, 2 * Math.PI, anticlockwise = false)
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
    var circleNr = 0
    while (radius > s.innerRadius) {
      plotRingOfDots(radius, circleNr)
      radius -= radius * change
      circleNr += 1
    }
  }
}