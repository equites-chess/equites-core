import sbt._
import Keys._

object BuildSettings {
  import Dependencies._
  import ScalariformSettings._

  lazy val commonSettings = Seq(
    version := "0.0.0",

    homepage := Some(url("http://equites.timepit.eu")),

    startYear := Some(2011),

    licenses += "GPLv3" -> url("http://www.gnu.org/licenses/gpl-3.0.html"),

    scmInfo := Some(ScmInfo(url("https://github.com/fthomas/equites"),
                    "git@github.com:fthomas/equites.git")),

    scalaVersion := "2.10.3",

    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xfatal-warnings",
      "-Ywarn-all"
    ),

    scalacOptions in (Compile, doc) ++= Seq(
      "-diagrams",
      "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
      "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
    ),

    autoAPIMappings := true
  )

  lazy val childSettings =
    de.johoop.cpd4sbt.CopyPasteDetector.cpdSettings ++
    org.scalastyle.sbt.ScalastylePlugin.Settings ++
    //scct.ScctPlugin.instrumentSettings ++
    ourScalariformSettings ++
    Seq(
      initialCommands := """
        import scalaz._
        import Scalaz._
        import eu.timepit.equites._
      """
    )

  lazy val rootSettings =
    play.Project.playScalaSettings ++
    //scct.ScctPlugin.mergeReportSettings ++
    commonSettings ++
    Seq(
      publishArtifact := false
    )

  lazy val cliSettings =
    commonSettings ++
    childSettings ++
    Seq(
      libraryDependencies ++= Seq(
        scalazStream
      )
    )

  lazy val coreSettings =
    commonSettings ++
    childSettings ++
    Seq(
      libraryDependencies ++= Seq(
        scalazCore,
        scalacheck % "test",
        scalazScalacheckBinding % "test",
        specs2 % "test"
      )
    )

  lazy val webSettings =
    play.Project.playScalaSettings ++
    commonSettings ++
    childSettings
}
