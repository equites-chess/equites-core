import sbt._
import Keys._

import org.scalastyle.sbt.ScalastylePlugin

object BuildSettings {
  lazy val basicSettings = ScalastylePlugin.Settings ++ seq(
    version := "0.0",
    homepage := Some(url("http://equites.timepit.eu")),
    startYear := Some(2011),
    licenses += "GPLv3" -> url("http://www.gnu.org/licenses/gpl-3.0.html"),
    scmInfo := Some(ScmInfo(url("https://github.com/fthomas/equites"),
                    "git@github.com:fthomas/equites.git")),

    scalaVersion := "2.10.1",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xfatal-warnings"
    ),
    scalacOptions in (Compile, doc) ++= Seq(
      "-diagrams"
    ),
    scalacOptions in (Compile, doc) <++=
      (baseDirectory in LocalProject("root"), scmInfo).map { (bd, scm) =>
        Seq("-sourcepath", bd.getAbsolutePath,
            "-doc-source-url", scm.get.browseUrl + "/tree/master€{FILE_PATH}.scala")
      }
  )

  lazy val coreSettings = basicSettings ++ seq(
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.10.1+" % "test",
      "org.scalaz" %% "scalaz-scalacheck-binding" % "7+" % "test",
      "org.specs2" %% "specs2" % "1.14+" % "test"
    ),

    initialCommands := """
      import scalaz._
      import Scalaz._
      import eu.timepit.equites._
      """
  )
}
