import sbt._
import Keys._

object EquitesBuild extends Build {
  import BuildSettings._

  lazy val root = Project(id = "equites-root", base = file("."))
    .settings(rootSettings: _*)
    .aggregate(core, web)

  lazy val core = Project(id = "equites-core", base = file("core"))
    .settings(coreSettings: _*)

  lazy val web = Project(id = "equites-web", base = file("web"))
    .settings(webSettings: _*)
    .dependsOn(core)
}
