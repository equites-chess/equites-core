import sbt._
import Keys._

object BuildSettings {
  import Dependencies._

  lazy val commonSettings = Seq(
    organization := "eu.timepit",

    version := "0.0.0",

    homepage := Some(url("http://equites.timepit.eu")),

    startYear := Some(2011),

    licenses += "GPL-3.0" -> url("http://www.gnu.org/licenses/gpl-3.0.html"),

    scmInfo := Some(ScmInfo(url("https://github.com/fthomas/equites"),
                    "git@github.com:fthomas/equites.git")),

    scalaVersion := "2.11.4",

    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint"
    ),

    scalacOptions in (Compile, doc) ++= Seq(
      "-diagrams",
      "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
      "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
    ),

    autoAPIMappings := true,

    resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
  )

  lazy val childSettings =
    bintray.Plugin.bintraySettings ++
    de.johoop.cpd4sbt.CopyPasteDetector.cpdSettings ++
    ScalariformSettings.ourScalariformSettings ++
    scoverage.ScoverageSbtPlugin.instrumentSettings ++
    Seq(
      initialCommands := """
        import scalaz._
        import Scalaz._
        import scalaz.stream._
        import eu.timepit.equites._
      """
    )

  lazy val rootSettings =
    commonSettings ++
    Seq(
      publishArtifact := false
    )

  lazy val cliSettings =
    commonSettings ++
    childSettings

  lazy val coreSettings =
    commonSettings ++
    childSettings ++
    Seq(
      libraryDependencies ++= Seq(
        scalazConcurrent,
        scalazCore,
        scalazStream,
        shapeless,
        scalacheck % "test",
        scalazScalacheckBinding % "test"
      ) ++ specs2.map(_ % "test")
    )

  lazy val gfxSettings =
    commonSettings ++
    childSettings
}
