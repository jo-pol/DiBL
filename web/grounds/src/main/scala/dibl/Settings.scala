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

import org.scalajs.dom.raw.Console
import scala.collection.immutable.IndexedSeq
import scala.util.Try

/**
 * @param template  The base name (no path, no extension) of an SVG document.
 * @param stitches  A map of use-element-labels ("A1"-"C3" for a 3x3 matrix) in the group labeled "base tile"
 *                  to group-element-labels (e.g. "tc (0,1,1,0,-1,-1)") in the group labeled "pile".
 *
 *                  The permutations of "tcp" imply the twist/cross/pin actions composing a stitch.
 *                  A stitch is made with four threads at the nodes of a two-in two-out directed graph.
 *                  The tuple specifies the orientation of the four 'legs' of a stitch, e.g. like: |<, >|, ><, _V_
 *                  See also <a href="https://github.com/jo-pol/DiBL/wiki/Input-Files">matrices and tuples</a>.
 */
case class Settings(template: String, stitches: Map[String, String]) {
  override def toString = s"TEMPLATE: $template STITCHES: $stitches"
}

object Settings {

  /** Interprets the query part of a URI: "key1=value1&key2=value2&..." */
  def parseUri(uri: String)(implicit console: Console, debug: Boolean): Settings = {

    val queryMap: Map[String, String] = (for {s <- uri.replaceAll("^[^?]+[?]", "").replace("?", "").split("&")}
      yield (s.replaceAll("=.*", ""), s.replaceAll("^[^=]+=*", ""))
      ).toMap
    val template: String = queryMap.getOrElse("template", "diagonal-3x3-thread")
    if (debug) console.info(s"$template $uri ${queryMap.toArray.deep.toString()}")

    val graph: M = {
      val pattern: Int = Try(queryMap.getOrElse("pattern", "0").toInt).getOrElse(-1)
      val fallBack = M(R("", "", "", ""), R("", "", "", ""), R("", "", "", ""), R("", "", "", ""))
      val flip: String = queryMap.getOrElse("flip", "")
      val m: M = Graphs.get(template, pattern, fallBack)
      flip match {
        case "x"|"y"|"r" => template.replaceFirst("-.*$", "") match {
          case "interleaved" => MatrixFlipper.flipInterleaved(flip, m)
          case "diagonal" => MatrixFlipper.flipDiamond(flip, m)
          case "brick" => MatrixFlipper.flipBrick(flip, m) // FIXME: along X not OK for pattern 5
          case _ => m
        }
        case _ => m
      }
    }

    val tuples: IndexedSeq[(String, String)] = for {
      r <- graph.indices
      c <- graph(0).indices
    } yield {
        val key: String = s"${"ABCD".substring(c, c + 1)}${r + 1}"
        val stitch: String = queryMap.getOrElse(key, "tc")
        (key, s"$stitch (${graph(r)(c)})")
      }
    new Settings(template, tuples.toArray.toMap)
    {
      override def toString = s"${super.toString} FROM: $uri"
    }
  }
}