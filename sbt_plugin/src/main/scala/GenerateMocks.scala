// Copyright (c) 2011 Paul Butcher
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import sbt._

trait GenerateMocks extends DefaultProject {

  def managedSources = "src_managed"
  override def cleanAction = super.cleanAction dependsOn cleanTask(managedSources)
  
  def generateMocksSourceRoots = "src" / "generate_mocks" / "scala"
  def generateMocksSources = sources(generateMocksSourceRoots)
  def generateMocksCompilePath = outputPath / "generatemock-classes"
  def generateMocksAnalysisPath = outputPath / "generatemock-analysis"

  def managedMockSource = managedSources / "mock" / "scala"
  def managedTestSource = managedSources / "test" / "scala"
  
  def generateMockCompileOptions = 
    compileOptions(
      "-Xplugin:compiler_plugin/target/scala_"+ buildScalaVersion +"/compiler-plugin_"+ buildScalaVersion +"-"+ projectVersion.value +".jar",
      "-Xplugin-require:borachio",
      // "-Ylog:generatemocks",
      "-Ystop-after:generatemocks",
      "-P:borachio:generatemocks:"+ managedMockSource,
      "-P:borachio:generatetest:"+ managedTestSource
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
