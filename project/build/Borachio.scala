import sbt._ 

class Borachio(info: ProjectInfo) extends DefaultProject(info) { 
  val scalatest = 
    buildScalaVersion match {
      case "2.8.1" => "org.scalatest" % "scalatest" % "1.3"
      case "2.9.0" => "org.scalatest" %% "scalatest" % "1.4.1"
      case x => error("Unsupported Scala version " + x)
    } 
  val junit = "junit" % "junit" % "3.8.2"
  val specs2 = 
    buildScalaVersion match {
      case "2.8.1" => "org.specs2" %% "specs2" % "1.2"
      case "2.9.0" => "org.specs2" %% "specs2" % "1.3"
      case x => error("Unsupported Scala version " + x)
    } 
  
  override def managedStyle = ManagedStyle.Maven
  val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
  Credentials(Path.userHome / ".ivy2" / ".credentials", log)
  
  def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
  override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
}
