name := "Equites"

version := "0.0"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "org.scalaz" % "scalaz-core_2.10" % "7+",
    "org.specs2" % "specs2_2.10" % "1.13"
)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")
