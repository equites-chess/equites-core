name := "equites-core"
version := "0.0.0"

organization := "eu.timepit"
homepage := Some(url("http://equites.timepit.eu"))
startYear := Some(2011)
licenses += "GPL-3.0" -> url("http://www.gnu.org/licenses/gpl-3.0.html")
scmInfo := Some(ScmInfo(url("https://github.com/fthomas/equites"),
                        "git@github.com:fthomas/equites.git"))

scalaVersion := "2.11.6"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Yrangepos"
)

scalacOptions in (Compile, doc) ++= Seq(
  "-diagrams",
  "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
  "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
)

autoAPIMappings := true



    bintray.Plugin.bintraySettings ++
    ScalariformSettings.ourScalariformSettings


resolvers ++= Seq(
  "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases",
  "Frank's Bintray" at "https://dl.bintray.com/fthomas/maven"
)

val scalazVersion = "7.1.1"
val specs2Version = "3.4"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.0.0",
  "com.nicta" %% "rng" % "1.3.0",
  "eu.timepit" %% "scalaz-stream-contrib" % "master-bb646960961ec6aee8f53543d573d558db2ce386",
  "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
  "org.scalaz" %% "scalaz-core" % scalazVersion,
  "org.scalaz.stream" %% "scalaz-stream" % "0.7a",

  "org.scalacheck" %% "scalacheck" % "1.12.2" % "test",
  "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test",
  "org.specs2" %% "specs2-core"          % specs2Version % "test",
  "org.specs2" %% "specs2-scalacheck"    % specs2Version % "test",
  "org.specs2" %% "specs2-matcher-extra" % specs2Version % "test"
)

initialCommands := """
  import scalaz._
  import scalaz.Scalaz._
  import scalaz.stream._
  import eu.timepit.equites._
"""
