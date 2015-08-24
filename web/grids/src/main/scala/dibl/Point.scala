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
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/

package dibl

/**
 * Created by jokepol on 17/05/15.
 */
case class Point(ringRadius: Double, dotNr: Int, ringNr: Int, s: Settings) {

  // plus 1 to keep dots on the edge completely on the canvas
  // plus radius to show the full circle, not just the south-east quart

  val x = ringRadius * Math.cos(a) + s.outerRadius + s.maxArc / 2

  val y = ringRadius * Math.sin(a) + s.outerRadius + s.maxArc / 2

  /** distance between two dots along the circle */
  val arcLength = Math.PI * ringRadius / s.dotsPerRing

  private def a = (dotNr + (ringNr % 2) * 0.5) * s.alpha
}
