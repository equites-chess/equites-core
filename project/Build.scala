import sbt._
import Keys._

object EquitesBuild extends Build {
  import BuildSettings._

  lazy val root = Project(id = "root", base = file("."))
    .aggregate(core)
    .settings(basicSettings: _*)

  lazy val core = Project(id = "core", base = file("core"))
    .settings(coreSettings: _*)
}
