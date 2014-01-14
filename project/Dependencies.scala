import sbt._
import Keys._

object Dependencies {
  val scalazVersion = "7.0.5"

  val scalacheck =
    "org.scalacheck" %% "scalacheck" % "1.10.1"
  val scalazCore =
    "org.scalaz" %% "scalaz-core" % scalazVersion
  val scalazScalacheckBinding =
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion
  val scalazStream =
    "org.scalaz.stream" %% "scalaz-stream" % "0.3"
  val shapeless =
    "com.chuusai" % "shapeless" % "2.0.0-M1" cross CrossVersion.full
  val specs2 =
    "org.specs2" %% "specs2" % "2.2.2"
  val swaggerPlay =
    "com.wordnik" %% "swagger-play2" % "1.3.2"
}
