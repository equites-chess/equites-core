import sbt._
import Keys._

object EquitesBuild extends Build {
  import BuildSettings._

  lazy val root = Project(id = "root", base = file("."))
    .aggregate(core)
    .settings(basicSettings: _*)
    .settings(ScctPlugin.mergeReportSettings: _*)

  lazy val core = Project(id = "core", base = file("core"))
    .settings(coreSettings: _*)
    .settings(ScctPlugin.instrumentSettings: _*)
}
