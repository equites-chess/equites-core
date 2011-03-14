import sbt._

class EquitesProject(info: ProjectInfo) extends DefaultProject(info) {
  override def mainScalaSourcePath = "src"
  override def mainResourcesPath   = "resources"

  override def testScalaSourcePath = "test"
  override def testResourcesPath   = "test-resources"
}
