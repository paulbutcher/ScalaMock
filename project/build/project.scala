import sbt._ 

class Borachio(info: ProjectInfo) extends ParentProject(info) {
  
  lazy val library = project("library", "Borachio Library", new LibraryProject(_))
  lazy val plugin = project("plugin", "Borachio Plugin", new PluginProject(_), library)
  lazy val plugin_test = project("plugin_test", "Borachio Plugin Test", new PluginTestProject(_), library, plugin)
  
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
  
  class PluginTestProject(info: ProjectInfo) extends DefaultProject(info) {

    val scalatest = "org.scalatest" %% "scalatest" % "1.4.1" % "optional"
    
    var generatingMocks = false
    
    override def compileOptions = 
      if (generatingMocks)
        compileOptions(
          "-Xplugin:plugin/target/scala_"+ buildScalaVersion +"/borachio-plugin_"+ buildScalaVersion +"-"+ projectVersion.value +".jar",
          "-Xplugin-require:borachio",
          "-P:borachio:generatemocks:plugin_test/src_managed/test/scala"
        ) ++ super.compileOptions
      else
        super.compileOptions
    
    lazy val generateMocks = generateMocksAction
    def generateMocksAction = generateMocksTask describedAs "Generates sources for classes with the @mock annotation"
    def generateMocksTask = task {
      generatingMocks = true
      testCompileConditional.run
      generatingMocks = false
      None
    }
  }
}
