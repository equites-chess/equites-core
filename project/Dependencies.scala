import sbt._
import Keys._

object Dependencies {
  val scalazVersion = "7.0.6"

  val scalacheck =
    "org.scalacheck" %% "scalacheck" % "1.10.1"
  val scalazConcurrent =
    "org.scalaz" %% "scalaz-concurrent" % scalazVersion
  val scalazCore =
    "org.scalaz" %% "scalaz-core" % scalazVersion
  val scalazScalacheckBinding =
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion
  val scalazStream =
    "org.scalaz.stream" %% "scalaz-stream" % "0.3.1"
  val shapeless =
    "com.chuusai" % "shapeless" % "2.0.0" cross CrossVersion.full
  val specs2 =
    "org.specs2" %% "specs2" % "2.2.2"
  val swaggerPlay =
    "com.wordnik" %% "swagger-play2" % "1.3.2"
}
