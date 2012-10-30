// Copyright (c) 2011-2012 Paul Butcher
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

object BuildSettings {
  val buildVersion = "3.0-M5"
  val buildScalaVersion = "2.10.0-RC1"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.scalamock",
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    scalaBinaryVersion := buildScalaVersion,
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    scalacOptions in (Compile, doc) ++= Opts.doc.title("ScalaMock") ++ Opts.doc.version(buildVersion) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
    resolvers += Resolver.sonatypeRepo("releases"),
    resolvers += Resolver.sonatypeRepo("snapshots"),

    publishTo <<= version { v =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots") 
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomIncludeRepository := { _ => false },
    publishArtifact in Test := false,
    pomExtra := (
      <url>http://scalamock.org/</url>
      <licenses>
        <license>
          <name>BSD-style</name>
          <url>http://www.opensource.org/licenses/bsd-license.php</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:paulbutcher/ScalaMock.git</url>
        <connection>scm:git:git@github.com:paulbutcher/ScalaMock.git</connection>
      </scm>
      <developers>
        <developer>
          <id>paulbutcher</id>
          <name>Paul Butcher</name>
          <url>http://paulbutcher.com/</url>
        </developer>
      </developers>),
  
    shellPrompt := ShellPrompt.buildShellPrompt
  )
}

object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
  )

  val buildShellPrompt = { 
    (state: State) => {
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (
        currProject, currBranch, BuildSettings.buildVersion
      )
    }
  }
}

object Dependencies {
  val scalatest = "org.scalatest" % "scalatest_2.10.0-RC1" % "2.0.M4"
  val specs2 = "org.specs2" % "specs2_2.10.0-RC1" % "1.12.2"
  val reflect = "org.scala-lang" % "scala-reflect" % BuildSettings.buildScalaVersion
  val actors = "org.scala-lang" % "scala-actors" % BuildSettings.buildScalaVersion
}

object ScalaMockBuild extends Build {
  import BuildSettings._
  import Dependencies._

  lazy val scalamock = Project(
    "ScalaMock", 
    file("."),
    settings = buildSettings ++ Seq(
      compile in Compile := Analysis.Empty,
      publishArtifact in (Compile, packageBin) := false,
      publishArtifact in (Compile, packageSrc) := false,
      sources in Compile <<= (Seq(core, scalatestSupport, specs2Support).map(sources in Compile in _).join).map(_.flatten),
      libraryDependencies ++= Seq(reflect, scalatest, specs2)
    )) aggregate(core, core_tests, scalatestSupport, specs2Support, examples)

  lazy val core = Project(
    "core", 
    file("core"),
    settings = buildSettings ++ Seq(
      name := "ScalaMock Core",
	  libraryDependencies ++= Seq(reflect, actors)
    ))

  lazy val scalatestSupport = Project(
    "scalatest", 
    file("frameworks/scalatest"),
    settings = buildSettings ++ Seq(
      name := "ScalaMock ScalaTest Support",
      libraryDependencies ++= Seq(scalatest)
    )) dependsOn(core)

  lazy val specs2Support = Project(
    "specs2", 
    file("frameworks/specs2"),
    settings = buildSettings ++ Seq(
      name := "ScalaMock Specs2 Support",
      libraryDependencies += specs2
    )) dependsOn(core)

  lazy val core_tests = Project(
    "core_tests", 
    file("core_tests"),
    settings = buildSettings ++ Seq(
      name := "ScalaMock Core Tests",
      publish := (),
      publishLocal := ()
    )) dependsOn(scalatestSupport)
    
  lazy val examples = Project(
    "examples",
    file("examples"),
    settings = buildSettings ++ Seq(
      name := "ScalaMock Examples",
      publish := (),
      publishLocal := ()
    )) dependsOn(scalatestSupport, specs2Support)
}