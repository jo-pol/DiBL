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
