import sbt._ 

class Borachio(info: ProjectInfo) extends ParentProject(info) {
  
  lazy val library = project("library", "Borachio Library", new LibraryProject(_))
  lazy val plugin = project("plugin", "Borachio Plugin", new PluginProject(_), library)
  
  class LibraryProject(info: ProjectInfo) extends DefaultProject(info) {

    val scalatest = "org.scalatest" %% "scalatest" % "1.4.1" % "optional"
    val junit = "junit" % "junit" % "3.8.2" % "optional"
    val specs2 = "org.specs2" %% "specs2" % "1.3" % "optional"

    def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
    override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
  
    override def managedStyle = ManagedStyle.Maven
    val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/"
    Credentials(Path.userHome / ".ivy2" / ".credentials", log)
  }
  
  class PluginProject(info: ProjectInfo) extends DefaultProject(info)
}
