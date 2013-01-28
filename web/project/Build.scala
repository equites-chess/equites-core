import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  lazy val equites = RootProject(file(".."))

  val appName         = "Equites-web"
  val appVersion      = "0.0"

  val appDependencies = Seq(
    // Add your project dependencies here,
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  ).dependsOn(equites)
}
