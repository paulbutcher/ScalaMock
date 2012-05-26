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

object ScalaMockBuild extends Build {

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.scalamock",
    version := "3.0-SNAPSHOT",
    scalaVersion := "2.10.0-M3",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
    scalacOptions in (Compile, console) += "-Yreify-copypaste",
    resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",

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
      <url>http://paulbutcher.com/</url>
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
      </developers>)
  )

  lazy val scalamock = Project(
    "ScalaMock", 
    file("."),
    settings = buildSettings ++ Seq(
      compile in Compile := Analysis.Empty,
      publishArtifact in (Compile, packageBin) := false,
      publishArtifact in (Compile, packageSrc) := false,
      sources in Compile <<= (Seq(core, scalatest).map(sources in Compile in _).join).map(_.flatten)
    )) aggregate(core, core_tests, scalatest)

  lazy val core = Project(
    "core", 
    file("core"),
    settings = buildSettings ++ Seq(
      name := "ScalaMock Core"
    ))

  lazy val scalatest = Project(
    "scalatest", 
    file("frameworks/scalatest"),
    settings = buildSettings ++ Seq(
      name := "ScalaMock ScalaTest Support",
      libraryDependencies += "org.scalatest" %% "scalatest" % "1.8-SNAPSHOT"
    )) dependsOn(core)

  lazy val core_tests = Project(
    "core_tests", 
    file("core_tests"),
    settings = buildSettings ++ Seq(
      name := "ScalaMock Core Tests",
      publish := (),
      publishLocal := ()
    )) dependsOn(scalatest, core)
}