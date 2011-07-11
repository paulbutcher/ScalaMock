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

class Borachio(info: ProjectInfo) extends ParentProject(info) {
  
  val scalaToolsSnapshots = ScalaToolsSnapshots

  override def managedStyle = ManagedStyle.Maven
  val publishTo = "Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/snapshots/"
  Credentials(Path.userHome / ".ivy2" / ".credentials", log)
  
  lazy val library = project("library", "Library", new BorachioProject(_))
  lazy val compiler_plugin = project("compiler_plugin", "Compiler Plugin", new BorachioProject(_), library)
  lazy val examples = project("examples", "Examples", new BorachioProject(_), library, compiler_plugin)
  lazy val compiler_plugin_tests = project("compiler_plugin_tests", "Compiler Plugin Tests", new BorachioProject(_), library, compiler_plugin)
  
  class BorachioProject(info: ProjectInfo) extends DefaultProject(info) with GenerateMocks {

    val scalatest = "org.scalatest" %% "scalatest" % "1.6.1" % "optional"
    val junit = "junit" % "junit" % "3.8.2" % "optional"

    override def compilerPlugin = "compiler_plugin/target/scala_"+ buildScalaVersion +"/compiler-plugin_"+ buildScalaVersion +"-2.0-SNAPSHOT.jar"

    override def compileOptions = super.compileOptions ++ Seq(Unchecked) ++ compileOptions("-Xfatal-warnings")

    override def generateMockCompileOptions = super.generateMockCompileOptions ++ compileOptions("-Ylog:generatemocks")
  }

  lazy val sbt_plugin = project("sbt_plugin", "sbt Plugin", new SbtPluginProject(_))
  
  class SbtPluginProject(info: ProjectInfo) extends PluginProject(info)
}
