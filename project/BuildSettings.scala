import sbt._
import Keys._

object BuildSettings {
  lazy val basicSettings = seq(
    scalaVersion := "2.10.0"
  )

  lazy val coreSettings = basicSettings ++ seq(
    initialCommands := "import eu.timepit.equites._"
  )
}
