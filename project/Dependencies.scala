import sbt._
import Keys._

object Dependencies {
  val scalazVersion = "7.0.4"

  val scalacheck =
    "org.scalacheck" %% "scalacheck" % "1.10.1"
  val scalazCore =
    "org.scalaz" %% "scalaz-core" % scalazVersion
  val scalazScalacheckBinding =
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion
  val scalazStream =
    "org.scalaz.stream" %% "scalaz-stream" % "0.1"
  val specs2 =
    "org.specs2" %% "specs2" % "2.2.2"
}
