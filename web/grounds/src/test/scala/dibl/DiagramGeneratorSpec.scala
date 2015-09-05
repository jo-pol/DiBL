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

import java.io._
import java.net.URL

import org.scalajs.dom.raw.{Console, DOMParser, Document, XMLSerializer}
import org.scalatest.{FlatSpec, Matchers}

class DiagramGeneratorSpec extends FlatSpec with Matchers {

  implicit val console: Console = null
  implicit val debug: Boolean = false

  ignore should "???" in {
    val settings: Settings = Settings.parseUri(
      "template=flanders-3x2-pair&flip=x&A1=ctc&A2=tctc&A3=lc&B1=ctc&B2=ctc&B3=cr"
    )
    val doc = readTemplate(settings.template)
    new DiagramGenerator(settings).apply(doc)
    new FileOutputStream("target/flanders.svg").write(
      new XMLSerializer().serializeToString(doc).getBytes
    )
  }

  def readTemplate(baseName: String): Document = {
    val reader = new BufferedReader(new InputStreamReader(new URL(
      s"https://github.com/jo-pol/DiBL/blob/gh-pages/grounds/templates/$baseName.svg"
    ).openStream))
    val sb = new StringBuilder()
    try {
      var inputLine = reader.readLine
      while (inputLine != null) {
        sb.append(inputLine.trim)
        inputLine = reader.readLine
      }
    } finally reader.close()
    new DOMParser().parseFromString(sb.result(), "text/xml") // FIXME
    // A method defined in a JavaScript raw type of a Scala.js library has been called.
    // This is most likely because you tried to run Scala.js binaries on the JVM.
    // Make sure you are using the JVM version of the libraries.
  }
}
