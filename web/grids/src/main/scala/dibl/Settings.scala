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

import org.scalajs.dom.Node

import scalatags.JsDom.all._

case class Settings(q: String) {

  /** The radius of the outer ring of dots converted from mm to pixels.
    *
    * The actually generated diameter will be an exact value.
    */
  val outerRadius: Double = 170 / 2 * scale

  /** The radius of the inner ring of dots converted from mm to pixels.
    *
    * The actually generated diameter may be slightly larger, depending an angle and number of dots.
    * */
  val innerRadius: Double = 100 / 2 * scale

  /** number of dots per ring */
  val dotsPerRing: Int = 120

  /** Angle in radians between two consecutive dots on a ring and the nearest dot on the next ring. */
  val angle: Double = Math.toRadians(30)

  /** Angle in radians of a pie formed by the centre of the grid and two consecutive dots on a ring. */
  val alpha: Double = Math.toRadians(360.0 / dotsPerRing.toDouble)

  /** The length of the arc between two dots on the outer ring of the grid. */
  val maxArc: Double = Point(outerRadius, 0, 0, this).arcLength

  /** The size required to accommodate the grid. */
  val canvasSize = Math.round(outerRadius * 2 + maxArc).toInt

  /** parse uri query: "key1=value1&key2=value2&..." */
  // TODO convert to Map[String, String] to compute the now hard coded values, don't care about duplicates
  private val m: Array[(String, String)] = for {s <- q.split("&")} yield (s.replaceAll("=.*", ""), s.replaceAll("^[^=]+=*", ""))

  /** zero: skip this dot, one: normal dot, two: circle */
  def plotType(point: Point): Char = {
    val row = point.ringNr % dotPattern.length
    val col = point.dotNr % dotPattern(row).length
    dotPattern(row)(col)
  }

  private def dotPattern = "1210,1,01,1,1012,1,01,1".split(",")

  /** The query string and/or error/warning messages */
  def toNode: Node = q match {
    case null => p("Please specify a query. Example: " + usage).render
    case qs => div(p(qs), p(s"${m.deep.mkString("[", ", ", "]")}")).render
  }

  private def usage = "?outerRadius=170&innerRadius=100&angle=30&dotsPerRing=120&pattern=1,1210,1,01,1,1012,1,01"

  /** DPI / mm; see https://developer.mozilla.org/en-US/docs/Web/API/HTMLCanvasElement/toDataURL */
  private def scale = 96 / 25.4
}
