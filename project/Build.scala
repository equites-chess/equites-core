import sbt._
import Keys._

object EquitesBuild extends Build {
  import BuildSettings._

  lazy val root = Project(id = "equites-root", base = file("."))
    .settings(rootSettings: _*)
    .aggregate(core)

  lazy val core = Project(id = "equites-core", base = file("core"))
    .settings(coreSettings: _*)
}
