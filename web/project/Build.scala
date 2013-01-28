import sbt._
import Keys._
import play.Project._

object EquitesWebBuild extends Build {
  lazy val equites = RootProject(file("../core"))

  val appName         = "Equites-web"
  val appVersion      = "0.0"

  val appDependencies = Seq(
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
  ).dependsOn(equites)
}
