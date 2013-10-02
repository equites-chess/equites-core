import sbt._
import Keys._

object EquitesBuild extends Build {
  import BuildSettings._
  import ScalariformSettings._

  lazy val root = Project(id = "root", base = file("."))
    .aggregate(core, web)
    .settings(basicSettings: _*)
    .settings(play.Project.playScalaSettings: _*)
    .settings(scct.ScctPlugin.mergeReportSettings: _*)
    //.settings(com.github.theon.coveralls.CoverallsPlugin.coverallsSettings: _*)

  lazy val core = Project(id = "core", base = file("core"))
    .settings(coreSettings: _*)
    .settings(ourScalariformSettings: _*)
    .settings(scct.ScctPlugin.instrumentSettings: _*)
    .settings(org.scalastyle.sbt.ScalastylePlugin.Settings: _*)
    .settings(de.johoop.cpd4sbt.CopyPasteDetector.cpdSettings: _*)

  lazy val web = Project(id = "web", base = file("web"))
    .settings(play.Project.playScalaSettings: _*)
    .dependsOn(core)
}
