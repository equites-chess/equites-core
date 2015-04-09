import sbt._
import Keys._

object Dependencies {
  val scalazVersion = "7.1.1"
  val specs2Version = "3.4"

  val scalacheck =
    "org.scalacheck" %% "scalacheck" % "1.12.2"
  val scalazConcurrent =
    "org.scalaz" %% "scalaz-concurrent" % scalazVersion
  val scalazCore =
    "org.scalaz" %% "scalaz-core" % scalazVersion
  val scalazScalacheckBinding =
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion
  val scalazStream =
    "org.scalaz.stream" %% "scalaz-stream" % "0.7a"
  val scalazStreamContrib =
    "eu.timepit" %% "scalaz-stream-contrib" % "0.0.0"
  val shapeless =
    "com.chuusai" %% "shapeless" % "2.0.0"
  val specs2 = Seq(
    "org.specs2" %% "specs2-core"          % specs2Version,
    "org.specs2" %% "specs2-scalacheck"    % specs2Version,
    "org.specs2" %% "specs2-matcher-extra" % specs2Version
  )
}
