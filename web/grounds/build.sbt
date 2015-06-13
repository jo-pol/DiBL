(emitSourceMaps in fullOptJS) := false

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "DiBL-ground"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"
//libraryDependencies += "org.scala-lang.modules" %%% "scala-xml" % "1.0.3"

bootSnippet := "dibl.Ground().main(document, matrices);"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

