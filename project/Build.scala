import sbt._
import Keys._

object EquitesBuild extends Build {
  import BuildSettings._

  lazy val root = Project("root", file("."))
    .aggregate(core)
    .settings(basicSettings: _*)
    .settings(ScctPlugin.mergeReportSettings: _*)
    .settings(com.github.theon.coveralls.CoverallsPlugin.coverallsSettings: _*)

  lazy val core = Project("core", file("core"))
    .settings(coreSettings: _*)
    .settings(ScctPlugin.instrumentSettings: _*)
}
