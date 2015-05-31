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

case class Settings(uriQuery: String) {

  /** parse uri query: "key1=value1&key2=value2&..." */
  val queryMap: Map[String, Array[String]] = {
    val m: Array[(String, String)] = for {s <- uriQuery.split("&")}
      yield (s.replaceAll("=.*", ""), s.replaceAll("^[^=]+=*", ""))
    m.groupBy(_._1).map { case (k, v) => (k, v.map(_._2)) }
  }

  val template: String = getArg("template","flanders.svg")
  
  val stitches: Map[String, String] = {
    val m: Array[(String, String)] = 
	  for {s <-Array("A1,A2,A3,A4,B1,B2,B3,B4,C1,C2,C3,C4,D1,D2,D3,D4").split("&")}
	    yield (s,getArg(s,"tc"))
    m.groupBy(_._1).map { case (k, v) => (k, v.map(_._2)(0)) }
  }	
  
  private def getArg(key: String, default: String) = {

    queryMap.get(key) match {
      case Some(a: Array[String]) => a(0)
      case _ => default
    }
  }
}
