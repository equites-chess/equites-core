import sbt._
import Keys._

object Dependencies {
  val scalazVersion = "7.1.0"

  val scalacheck =
    "org.scalacheck" %% "scalacheck" % "1.12.1"
  val scalazConcurrent =
    "org.scalaz" %% "scalaz-concurrent" % scalazVersion
  val scalazCore =
    "org.scalaz" %% "scalaz-core" % scalazVersion
  val scalazScalacheckBinding =
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion
  val scalazStream =
    "org.scalaz.stream" %% "scalaz-stream" % "0.5a"
  val shapeless =
    "com.chuusai" %% "shapeless" % "2.0.0"
  val specs2 = Seq(
      "org.specs2" %% "specs2-core"          % "3.0-M0",
      "org.specs2" %% "specs2-scalacheck"    % "3.0-M0",
      "org.specs2" %% "specs2-matcher-extra" % "3.0-M0"
      )

}
