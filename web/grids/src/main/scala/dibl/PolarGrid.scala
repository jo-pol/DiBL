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

import org.scalajs.dom
import org.scalajs.dom.html
import scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

case class Settings(q: String) {

  /** DPI / mm see https://developer.mozilla.org/en-US/docs/Web/API/HTMLCanvasElement/toDataURL */
  private val scale = 96 / 25.4

  /** parse uri query: "key1=value1&key2=value2&..." */
  // TODO convert to Map[String, String] to extract the values, don't bother duplicates
  private val m: Array[List[String]] = for {s <- q.split("&")} yield s.split("=").toList

  def outerDiameter = 170.0 * scale

  def innerDiameter = 100.0 * scale

  /** angle in radians between two dots on a ring and the nearest dot on the next ring */
  def angle = Math.toRadians(30)

  /** zero: skip this dot, one: normal dot, two: circle */
  def pattern = "1,1210,1,01,1,1012,1,01".split(",")

  /** angle in radians of a pie formed by the centre of the grid and two consecutive dots on a ring */
  def alpha = Math.toRadians(360.0 / dotsPerRing.toDouble)

  def dotsPerRing = 120
}

case class Point(radius: Double, dotNr: Int, ringNr: Int, s: Settings) {

  // plus 1 to keep dots on the edge completely on the canvas
  // plus radius to show the full circle, not just the south-east quart

  def x = radius * Math.cos(a) + s.outerDiameter / 2 + 1

  def y = radius * Math.sin(a) + s.outerDiameter / 2 + 1

  private def a = (dotNr + (ringNr % 2) * 0.5) * s.alpha

  /** distance between two dots along the circle */
  def arcLength = Math.PI * radius / s.dotsPerRing
}

@JSExport
object PolarGrid {
  @JSExport
  def main(target: html.Div): Unit = {

    val s = Settings(target.ownerDocument.documentURI.toString.replaceAll("^[^?]+.", ""))

    /** the radius changes each step with about half the arc length between two dots in case of an angle of 45 degrees */
    val change = {
      /** see https://github.com/jo-pol/DiBL/pull/5/files#diff-326bb6c119212f45e228f5e0516187e3R19 */
      val correction = Math.tan(s.angle * 0.93) * Math.PI / (4 * s.dotsPerRing)
      Math.tan(s.angle - correction) * Math.PI / s.dotsPerRing
    }
    val canvasContext = {
      val size = Math.round(s.outerDiameter + 2.0).toInt
      val gridCanvas = canvas().render
      while (target.hasChildNodes())
        target.removeChild(target.children.item(0))
      target.appendChild(p(s.q.replaceAll("&", " ")).render)
      target.appendChild(gridCanvas)
      gridCanvas.height = size
      gridCanvas.width = size
      gridCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    }
    def plotDot(point: Point) = {
      canvasContext.beginPath()
      canvasContext.arc(point.x, point.y, 1, 0, 2 * Math.PI, false)
      canvasContext.fill()
    }
    def plotCircle(point: Point) = {
      canvasContext.beginPath()
      canvasContext.arc(point.x, point.y, point.arcLength / 2, 0, 2 * Math.PI, false)
      canvasContext.stroke()
    }
    def plotRingOfDots(radius: Double, ringNr: Int) = {
      for (dotNr <- 0 until s.dotsPerRing) {
        val row = ringNr % s.pattern.length
        val option = s.pattern(row)(dotNr % s.pattern(row).length)
        val point = Point(radius, dotNr, ringNr, s)
        if (option == '1')
          plotDot(point)
        else if (option == '2')
          plotCircle(point)
      }
    }
    var diameter = s.outerDiameter
    var circleNr = 0
    while (diameter > s.innerDiameter) {
      plotRingOfDots(diameter / 2, circleNr)
      diameter -= diameter * change
      circleNr += 1
    }
  }
}
