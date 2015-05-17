(emitSourceMaps in fullOptJS) := false

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "DiBL-grids"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.4.6"

bootSnippet := "dibl.PolarGrid().main(document.getElementById('container'));"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

