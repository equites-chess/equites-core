import sbt._

class EquitesProject(info: ProjectInfo) extends DefaultProject(info) {
  // paths
  override def mainScalaSourcePath = "src"
  override def mainResourcesPath   = "resources"

  override def testScalaSourcePath = "test"
  override def testResourcesPath   = "test-resources"

  // scalaz
  val scalatoolsSnapshot = ScalaToolsSnapshots
  val scalazCore = "com.googlecode.scalaz" %% "scalaz-core" % "5.1-SNAPSHOT"

  // specs2
  val specs2 = "org.specs2" %% "specs2" % "1.0.1+"

  def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
  override def testFrameworks: Seq[TestFramework]
    = super.testFrameworks ++ Seq(specs2Framework)
}
