import sbt._
import Keys._

import org.scalastyle.sbt.ScalastylePlugin

object BuildSettings {
  lazy val basicSettings =
    ScalastylePlugin.Settings ++
    seq(
      scalaVersion := "2.10.0",
      scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")
    )

  lazy val coreSettings = basicSettings ++ seq(
    initialCommands := """
      import scalaz._
      import Scalaz._
      import eu.timepit.equites._
      """
  )
}
