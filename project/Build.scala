import sbt._
import Keys._

object EquitesBuild extends Build {
  lazy val core = Project(id = "core", base = file("core"))
  lazy val web  = Project(id = "web",  base = file("web")) dependsOn("core")
}
