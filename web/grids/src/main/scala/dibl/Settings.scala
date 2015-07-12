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

import scala.util.Try

case class Settings(uri: String) {

  /** DPI / mm; see https://developer.mozilla.org/en-US/docs/Web/API/HTMLCanvasElement/toDataURL */
  private val scale = 96 / 25.4

  /** parse uri query: "key1=value1&key2=value2&..." */
  val queryMap: Map[String, Array[String]] = {
    val m: Array[(String, String)] = for {s <- uri.replaceAll("[^?]+[?]", "").split("&")}
      yield (s.replaceAll("=.*", ""), s.replaceAll("^[^=]+=*", ""))
    m.groupBy(_._1).map { case (k, v) => (k, v.map(_._2)) }
  }

  /** The radius of the outer ring of dots converted from mm to pixels. */
  val outerDiameter: Double = getPosIntArg("outerDiameter", 170)

  /** The radius of the outer ring of dots converted from mm to pixels. */
  val outerRadius: Double = outerDiameter / 2 * scale

  /** The radius of the inner ring of dots converted from mm to pixels. */
  val innerDiameter: Double = Math.min(outerDiameter * 0.95, getPosIntArg("innerDiameter", 100))

  /** The radius of the inner ring of dots converted from mm to pixels. */
  val innerRadius: Double = innerDiameter / 2 * scale

  /** number of dots per ring */
  val dotsPerRing: Int = getPosIntArg("dotsPerRing", (outerRadius / 3).toInt)

  /** Angle in radians between two consecutive dots on a ring and the nearest dot on the next ring. */
  val angle: Double = Math.toRadians(getPosIntArg("angle", 30))

  /** Angle in radians of a pie formed by the centre of the grid and two consecutive dots on a ring. */
  val alpha: Double = Math.toRadians(360.0 / dotsPerRing.toDouble)

  /** The length of the arc between two dots on the outer ring of the grid. */
  val maxArc: Double = Point(outerRadius, 0, 0, this).arcLength

  /** The size required to accommodate the grid. */
  val canvasSize = Math.round(outerRadius * 2 + maxArc).toInt

  val dotPattern = getArg("dotPattern", "1").split("[,; ]")
  // TODO what is the subtle difference with the next?
  //private val dotPattern: Array[String] = (queryMap.getOrElse("dotPattern", Array("1")))(0).split(",; ")

  /** one: normal dot, two: circle, anything else: skip this dot  */
  def plotType(point: Point): Char = {
    val row = point.ringNr % dotPattern.length
    val col = point.dotNr % dotPattern(row).length
    dotPattern(row)(col)
  }

  private def getPosIntArg(key: String, default: Int): Int = {
    val value = Try(getArg(key, default.toString).toInt) toOption match {
      case Some(i: Int) => i
      case _ => default
    }
    if (value < 0) 0 - value else if (value == 0) default else value
  }

  private def getArg(key: String, default: String) = {

    queryMap.get(key) match {
      case Some(a: Array[String]) => a(0)
      case _ => default
    }
  }
}
