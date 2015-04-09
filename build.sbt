name := "equites-core"
version := "0.0.0"

organization := "eu.timepit"
homepage := Some(url("http://equites.timepit.eu"))
startYear := Some(2011)
licenses += "GPL-3.0" -> url("http://www.gnu.org/licenses/gpl-3.0.html")
scmInfo := Some(ScmInfo(url("https://github.com/fthomas/equites"),
                        "git@github.com:fthomas/equites.git"))

scalaVersion := "2.11.6"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Yrangepos"
)

scalacOptions in (Compile, doc) ++= Seq(
  "-diagrams",
  "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
  "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
)

autoAPIMappings := true

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"


    bintray.Plugin.bintraySettings ++
    ScalariformSettings.ourScalariformSettings ++
    Seq(
      initialCommands := """
        import scalaz._
        import Scalaz._
        import scalaz.stream._
        import eu.timepit.equites._
      """
    )

import Dependencies._

      libraryDependencies ++= Seq(
        scalazConcurrent,
        scalazCore,
        scalazStream,
        shapeless,
        scalacheck % "test",
        scalazScalacheckBinding % "test"
      ) ++ specs2.map(_ % "test")
    

