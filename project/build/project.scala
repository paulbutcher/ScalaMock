import sbt._ 

class Borachio(info: ProjectInfo) extends ParentProject(info) {
  
  val scalaToolsSnapshots = ScalaToolsSnapshots

  override def managedStyle = ManagedStyle.Maven
  val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/snapshots/"
  Credentials(Path.userHome / ".ivy2" / ".credentials", log)
  
  lazy val library = project("library", "Borachio Library", new LibraryProject(_))
  lazy val plugin = project("plugin", "Borachio Plugin", new PluginProject(_), library)
  lazy val examples = project("examples", "Borachio Examples", new ExamplesProject(_), library, plugin)
  
  class LibraryProject(info: ProjectInfo) extends DefaultProject(info) {

    val scalatest = "org.scalatest" %% "scalatest" % "1.4.1" % "optional"
    val junit = "junit" % "junit" % "3.8.2" % "optional"
  }
  
  class PluginProject(info: ProjectInfo) extends DefaultProject(info)
  
  class ExamplesProject(info: ProjectInfo) extends DefaultProject(info) {

    val scalatest = "org.scalatest" %% "scalatest" % "1.4.1"

    def managedSources = "src_managed"
    override def cleanAction = super.cleanAction dependsOn cleanTask(managedSources)
    
    def generateMocksSourceRoots = "src" / "generate_mocks" / "scala"
    def generateMocksSources = sources(generateMocksSourceRoots)
    def generateMocksCompilePath = outputPath / "generatemock-classes"
    def generateMocksAnalysisPath = outputPath / "generatemock-analysis"
    
    def generateMockCompileOptions = 
      compileOptions(
        "-Xplugin:plugin/target/scala_"+ buildScalaVersion +"/borachio-plugin_"+ buildScalaVersion +"-"+ projectVersion.value +".jar",
        "-Xplugin-require:borachio",
        // "-Ylog:generatemocks",
        "-Ystop-after:generatemocks",
        "-P:borachio:generatemocks:examples/src_managed/mock/scala",
        "-P:borachio:generatetest:examples/src_managed/test/scala"
      ) ++ super.compileOptions
    
  	class GenerateMocksCompileConfig extends TestCompileConfig {
  		override def baseCompileOptions = generateMockCompileOptions
  		override def label = "generatemocks"
      override def sourceRoots = generateMocksSourceRoots
  		override def sources = generateMocksSources
      override def outputDirectory = generateMocksCompilePath
      override def analysisPath = generateMocksAnalysisPath
  	}
  	def generateMocksCompileConfiguration = new GenerateMocksCompileConfig
  	lazy val generateMocksCompileConditional = new CompileConditional(generateMocksCompileConfiguration, buildCompiler)

    lazy val generateMocks = generateMocksAction
    def generateMocksAction = generateMocksTask describedAs "Generates sources for classes with the @mock annotation"
    def generateMocksTask = task {
      generateMocksCompileConditional.run
    }

    def managedMockSource = managedSources / "mock" / "scala"
    def managedTestSource = managedSources / "test" / "scala"
    def mockSourceRoots = managedMockSource +++ managedTestSource
    def mockSources = sources(mockSourceRoots)
    def mockCompilePath = outputPath / "mock-classes"
    def mockAnalysisPath = outputPath / "mock-analysis"
    
    class CompileMocksCompileConfig extends TestCompileConfig {
      override def label = "compilemocks"
      override def sourceRoots = mockSourceRoots
      override def sources = mockSources
      override def outputDirectory = mockCompilePath
      override def analysisPath = mockAnalysisPath
    }
    def compileMocksCompileConfiguration = new CompileMocksCompileConfig
    lazy val compileMocksCompileConditional = new CompileConditional(compileMocksCompileConfiguration, buildCompiler)
    
    lazy val compileMocks = compileMocksAction
    def compileMocksAction = compileMocksTask dependsOn generateMocksTask describedAs "Compile mocks"
    def compileMocksTask = task {
      compileMocksCompileConditional.run
    }
    
    override def testSourceRoots = super.testSourceRoots +++ managedTestSource
  }
}
