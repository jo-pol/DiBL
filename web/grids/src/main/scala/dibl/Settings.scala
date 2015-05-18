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

case class Settings(uriQuery: String) {

  /** The radius of the outer ring of dots converted from mm to pixels.
    *
    * The actually generated diameter will be an exact value.
    */
  val outerRadius: Double = getArg("outerRadius", "170").toInt / 2 * scale

  /** The radius of the inner ring of dots converted from mm to pixels.
    *
    * The actually generated diameter may be slightly larger, depending an angle and number of dots.
    * */
  val innerRadius: Double = getArg("innerRadius", "100").toInt / 2 * scale

  /** number of dots per ring */
  val dotsPerRing: Int = getArg("dotsPerRing", "120").toInt

  /** Angle in radians between two consecutive dots on a ring and the nearest dot on the next ring. */
  val angle: Double = Math.toRadians(getArg("angle", "30").toInt)

  /** Angle in radians of a pie formed by the centre of the grid and two consecutive dots on a ring. */
  val alpha: Double = Math.toRadians(360.0 / dotsPerRing.toDouble)

  /** The length of the arc between two dots on the outer ring of the grid. */
  val maxArc: Double = Point(outerRadius, 0, 0, this).arcLength

  /** The size required to accommodate the grid. */
  val canvasSize = Math.round(outerRadius * 2 + maxArc).toInt

  private val dotPattern = getArg("dotPattern", "1").split("[,; ]")
  // TODO what is the subtle difference with the next?
  //private val dotPattern: Array[String] = (queryMap.getOrElse("dotPattern", Array("1")))(0).split(",; ")

  /** one: normal dot, two: circle, anything else: skip this dot  */
  def plotType(point: Point): Char = {
    val row = point.ringNr % dotPattern.length
    val col = point.dotNr % dotPattern(row).length
    dotPattern(row)(col)
  }

  /** The query string and/or error/warning/log messages */
  def toNode: Node = uriQuery match {
    case null => p("Please specify a query. Example: " + usage).render // FIXME
    case qs => div(
      p(s"outerRadius: ${outerRadius*2/scale}"),
      p(s"innerRadius: ${innerRadius*2/scale}"),
      p(s"dotsPerRing: ${dotsPerRing.toString}"),
      p(s"angle: ${Math.toDegrees(angle)}"),
      p(s"dotPattern: ${dotPattern.deep.mkString("[", ", ", "]")}"),
      p(s"?${uriQuery}")
    ).render
  }

  /** parses uri query: "key1=value1&key2=value2&..." */
  private def getArg(key: String, default: String) = {
    // TODO how to prevent re-evaluation?
    // private values at a higher scope-level get reorganized after when needed when reformatting code by InteliJ
    val m1: Array[(String, String)] = for {s <- uriQuery.split("&")} yield (s.replaceAll("=.*", ""), s.replaceAll("^[^=]+=*", ""))
    val queryMap: Map[String, Array[String]] = m1.groupBy(_._1).map { case (k, v) => (k, v.map(_._2)) }

    queryMap.get(key) match {
      case None => default
      case Some(a: Array[String]) => a(0)
    }
  }

  private def usage = "?outerRadius=170&innerRadius=100&angle=30&dotsPerRing=120&pattern=1,1210,1,01,1,1012,1,01"

  /** DPI / mm; see https://developer.mozilla.org/en-US/docs/Web/API/HTMLCanvasElement/toDataURL */
  private def scale = 96 / 25.4
}
