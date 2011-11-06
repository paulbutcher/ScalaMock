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
import ScalaMockPlugin._

object ScalaMockBuild extends Build {
  
  lazy val versionSpecificDirectory = SettingKey[File]("version-specific-directory", "Version specific source code")
  
  override lazy val settings = super.settings ++ Seq(
    organization := "org.scalamock",
    version := "2.0",
    crossScalaVersions := Seq("2.8.1", "2.8.2", "2.9.0", "2.9.0-1", "2.9.1"),
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings"),

    publishTo <<= version { v =>
      val nexus = "http://nexus.scala-tools.org/content/repositories/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "snapshots/") 
      else
        Some("releases" at nexus + "releases/")
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
    publishArtifact in (Compile, packageDoc) := false
  )
  
  lazy val versionSpecificSettings = Seq(
    versionSpecificDirectory <<= (sourceDirectory, scalaVersion)((d, v) => d / "main" / ("scala-"+ majorVersion(v))),
    sources in Compile <++= collectSource(versionSpecificDirectory)
  )
    
  lazy val scalamock = Project("ScalaMock", file(".")) settings(
    compile in Mock := Analysis.Empty,
    compile in Compile := Analysis.Empty,
    publishArtifact in (Compile, packageBin) := false,
    publishArtifact in (Compile, packageSrc) := false,
    publishArtifact in (Compile, packageDoc) := true,
    publishArtifact in Test := false,
    libraryDependencies <+= scalaVersion("org.scalatest" %% "scalatest" % scalatestVersion(_)),
    libraryDependencies += "junit" % "junit" % "3.8.2",
    sources in Compile <<= (Seq(core, scalatest, junit3).map(sources in Compile in _).join).map(_.flatten)
  ) aggregate(core, core_tests, scalatest, junit3, compiler_plugin, compiler_plugin_tests
  ) configs(Mock)
  
  lazy val core = Project("core", file("core")) settings(versionSpecificSettings: _*) settings(
    name := "ScalaMock Core",
    
    // Workaround https://github.com/harrah/xsbt/issues/193
    unmanagedClasspath in Compile += Attributed.blank(new java.io.File("doesnotexist"))
  )
    
  lazy val scalatest: Project = Project("scalatest", file("frameworks/scalatest")) settings(
    name := "ScalaMock ScalaTest Support",
    libraryDependencies <+= scalaVersion("org.scalatest" %% "scalatest" % scalatestVersion(_))
  ) dependsOn(core)
  
  lazy val junit3: Project = Project("junit", file("frameworks/junit3")) settings(
    name := "ScalaMock JUnit3 Support",
    libraryDependencies += "junit" % "junit" % "3.8.2"
  ) dependsOn(core)
  
  lazy val core_tests: Project = Project("core_tests", file("core_tests"), 
    dependencies = Seq(scalatest % "test")) settings(publish := (), publishLocal := ())

  lazy val compiler_plugin = Project("compiler_plugin", file("compiler_plugin")) settings(
    versionSpecificSettings: _*) settings(
      name := "ScalaMock Compiler Plugin",
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)
    ) dependsOn(core)
    
  lazy val compiler_plugin_tests = Project("compiler_plugin_tests", file("compiler_plugin_tests")) settings(
    generateMocksSettings: _*) settings(
      publish := (),
      publishLocal := (),
      excludeFilter in unmanagedSources <<= scalaVersion( v => if (v == "2.9.1") "*_not_2.9.1.scala" else ""),
      scalacOptions in GenerateMocks <+= packageBin in (compiler_plugin, Compile) map { plug =>
        "-Xplugin:"+ plug.absolutePath
      }
    ) dependsOn(scalatest % "mock;test", compiler_plugin) configs(Mock)
    
  def majorVersion(scalaVersion: String) = if (scalaVersion startsWith "2.8") "2.8" else "2.9"
    
  val scalatestVersions = Map("2.8" -> "1.5.1", "2.9" -> "1.6.1")

  def scalatestVersion(scalaVersion: String) = getLibraryVersion(scalatestVersions, majorVersion(scalaVersion))

  def getLibraryVersion(versionMap: Map[String, String], version: String) =
    versionMap.getOrElse(version, sys.error("Unsupported Scala version: "+ version))
}
