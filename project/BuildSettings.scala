import sbt._
import Keys._

object BuildSettings {
  import Dependencies._

  lazy val basicSettings = seq(
    version := "0.0",
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
      "-diagrams"
    ),
    scalacOptions in (Compile, doc) <++=
      (baseDirectory in LocalProject("equites-root"), scmInfo).map { (bd, scm) =>
        Seq("-sourcepath", bd.getAbsolutePath,
            "-doc-source-url", scm.get.browseUrl + "/tree/master€{FILE_PATH}.scala")
      }
  )

  lazy val coreSettings = basicSettings ++ seq(
    libraryDependencies ++= Seq(
      scalazCore,
      scalacheck % "test",
      scalazScalaCheckBinding % "test",
      specs2 % "test"
    ),

    initialCommands := """
      import scalaz._
      import Scalaz._
      import eu.timepit.equites._
      """
  )

  lazy val webSettings = basicSettings ++ seq(
  )
}
