name := "Equites-core"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "org.scalaz" % "scalaz-core_2.10" % "7+",
    "org.scalaz" % "scalaz-scalacheck-binding_2.10" % "7+"
)

scalacOptions in (Compile, doc) ++= Seq("-diagrams")

scalacOptions in (Compile, doc) <++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/fthomas/equites/tree/master€{FILE_PATH}.scala"
  )
}
