import sbt._

class EquitesProject(info: ProjectInfo) extends DefaultProject(info) {
  override def mainScalaSourcePath = "src"
}
