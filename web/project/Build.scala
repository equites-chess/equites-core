import sbt._
import Keys._
import play.Project._

object EquitesWebBuild extends Build {
  lazy val core = RootProject(file("../core"))

  lazy val web = play.Project("equites-web")
    .dependsOn(core)
}
