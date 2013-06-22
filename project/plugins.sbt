resolvers ++= Seq(
  "scct-github-repository" at "http://mtkopone.github.com/scct/maven-repo",
  "sonatype-oss-repo" at "https://oss.sonatype.org/content/groups/public/"
)

addSbtPlugin("com.github.theon" %% "xsbt-coveralls-plugin" % "0.0.3-SNAPSHOT")

addSbtPlugin("com.orrsella" %% "sbt-stats" % "1.0.2")

addSbtPlugin("com.timushev.sbt" %% "sbt-updates" % "0.1.1")

addSbtPlugin("com.typesafe.sbteclipse" %% "sbteclipse-plugin" % "2.2.0")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.3.1")

addSbtPlugin("reaktor" %% "sbt-scct" % "0.2-SNAPSHOT")
