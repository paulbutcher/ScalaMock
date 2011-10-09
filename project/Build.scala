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
import Keys._
import sbt.inc.Analysis

object BorachioBuild extends Build {
  
  override lazy val settings = super.settings ++ Seq(
      organization := "com.borachio",
      version := "2.0-SNAPSHOT",
      scalaVersion := "2.9.0",
      scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings")
    )
    
  lazy val borachio = Project(
      "Borachio",
      file(".")
    ) settings (
      compile in Mock := Analysis.Empty,
      publish := ()
    ) aggregate(
      core, scalatest, junit3, compiler_plugin, compiler_plugin_tests
    ) configs(
      Mock
    )
  
  lazy val core = Project(
      "core", 
      file("core")
    ) settings(
      name := "Borachio Core"
    )
    
  lazy val scalatest: Project = Project("scalatest", file("frameworks/scalatest")) settings(
    name := "Borachio ScalaTest Support",
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1"
  ) dependsOn(core)
  
  lazy val junit3: Project = Project("junit", file("frameworks/junit3")) settings(
    name := "Borachio JUnit3 Support",
    libraryDependencies += "junit" % "junit" % "3.8.2"
  ) dependsOn(core)
  
  lazy val core_tests: Project = Project("core_tests", file("core_tests"), 
    dependencies = Seq(scalatest % "test")) settings(publish := ())

  lazy val compiler_plugin = Project(
      "compiler_plugin", 
      file("compiler_plugin")
    ) settings(
      name := "Borachio Compiler Plugin",
      libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.9.0"
    ) dependsOn(
      core
    )
    
  lazy val GenerateMocks = config("generate-mocks")
  lazy val Mock = config("mock") extend(Compile)
  
  lazy val generatedMockDirectory = SettingKey[File]("generated-mock-directory", "Where generated mock source code will be placed")
  lazy val generatedTestDirectory = SettingKey[File]("generated-test-directory", "Where generated test source code will be placed")
  
  lazy val generateMocks = TaskKey[Unit]("generate-mocks", "Generates sources for classes with the @mock annotation")
  def generateMocksTask = (sources in GenerateMocks, classDirectory in GenerateMocks, scalacOptions, 
      classpathOptions, scalaInstance, fullClasspath in Compile, streams, generatedMockDirectory, generatedTestDirectory) map {
    (srcs, out, initialOpts, cpOpts, si, cp, s, gm, gt) =>
      val opts = initialOpts ++ Seq(
        "-Xplugin:compiler_plugin/target/scala-2.9.0.final/borachio-compiler-plugin_2.9.0-2.0-SNAPSHOT.jar",
        "-Xplugin-require:borachio",
        "-Ylog:generatemocks",
        "-Ystop-after:generatemocks",
        "-P:borachio:generatemocks:"+ gm,
        "-P:borachio:generatetest:"+ gt)
      IO.delete(out)
      IO.createDirectory(out)
      val comp = new compiler.RawCompiler(si, cpOpts, s.log)
      comp(srcs, cp.files, out, opts)
  }
  
  def collectSource(directory: SettingKey[File]) =
    (directory, sourceFilter, defaultExcludes) map { (d, f, e) => d.descendentsExcept(f, e).get }
  
  lazy val generateMocksSettings = 
    inConfig(GenerateMocks)(Defaults.configSettings) ++ 
    inConfig(Mock)(Defaults.configSettings) ++
    Seq(
      publish := (),
      generatedMockDirectory <<= sourceManaged(_ / "mock" / "scala"),
      generatedTestDirectory <<= sourceManaged(_ / "test" / "scala"),
      sources in Test <++= collectSource(generatedTestDirectory),
      sources in Mock <++= collectSource(generatedMockDirectory),
      sources in Mock <++= collectSource(generatedTestDirectory),
      generateMocks <<= generateMocksTask)
    
  lazy val compiler_plugin_tests = Project(
      "compiler_plugin_tests", 
      file("compiler_plugin_tests")
    ) settings(
      generateMocksSettings: _*
    ) dependsOn(
      scalatest % "mock;test",
      compiler_plugin
    ) configs(
      Mock
    )
}
