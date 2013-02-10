name := "Equites-core"

version := "0.0"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "org.scalaz" % "scalaz-core_2.10" % "7+",
    "org.specs2" % "specs2_2.10" % "1.13"
)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

scalacOptions in (Compile, doc) ++= Seq("-diagrams")

scalacOptions in (Compile, doc) <++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/fthomas/equites/tree/master€{FILE_PATH}.scala"
  )
}

initialCommands := "import eu.timepit.equites._"
