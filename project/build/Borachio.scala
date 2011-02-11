import sbt._ 

class Borachio(info: ProjectInfo) extends DefaultProject(info) { 
  val scalatest = "org.scalatest" % "scalatest" % "1.2"
}
