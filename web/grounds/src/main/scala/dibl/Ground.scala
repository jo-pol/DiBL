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
import org.scalajs.dom.raw.Document
import org.scalajs.dom.html
import scalajs.js.annotation.JSExport

@JSExport
object Ground {
  @JSExport
  def main(document: Document, matricesOfTuples: Map[Array[Array[Array[Array[]]]]]): Unit = {

    val s = Settings(new URI(document.documentURI).getQuery match {
      case null => ""
      case s => s
    })
	
	val svg = Ajax.get(s.template)
	
	"//g[@inkscape:label='base tile']/use[@inkscape:label]"
	val cells = ... foreach yield {node => (node.attribute('inkscape:label') -> node) } 

	"//g[@inkscape:label='pile']/g[@inkscape:label]"
	val stitches = ... foreach yield {node => (node.attribute('inkscape:label') -> node.attribute('id')) } 
}
