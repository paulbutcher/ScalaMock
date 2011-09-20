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

object Borachio extends Build {

  override lazy val settings = super.settings ++ Seq(
    organization := "com.borachio",
    version := "1.3-SNAPSHOT",
    crossScalaVersions := Seq("2.8.1", "2.9.1"),

    scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings"),

    publishTo <<= version { v =>
      val nexus = "http://nexus.scala-tools.org/content/repositories/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "snapshots/") 
      else
        Some("releases" at nexus + "releases/")
    },
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
  )
  
  lazy val core = Project("Borachio core", file("core"), 
    aggregate = Seq(scalatest, specs2, junit3, core_tests)) dependsOn(dummy)

  lazy val scalatest: Project = Project("Borachio ScalaTest support", file("frameworks/scalatest")) settings(
    libraryDependencies <+= scalaVersion("org.scalatest" %% "scalatest" % scalatestVersion(_))
  ) dependsOn(core)

  lazy val specs2: Project = Project("Borachio Specs2 support", file("frameworks/specs2")) settings(
    libraryDependencies <+= scalaVersion("org.specs2" %% "specs2" % specs2Version(_))
  ) dependsOn(core)

  lazy val junit3: Project = Project("Borachio Junit3 support", file("frameworks/junit3")) settings(
    libraryDependencies += "junit" % "junit" % "3.8.2"
  ) dependsOn(core)
  
  lazy val core_tests: Project = Project("Tests", file("core_tests"), 
    dependencies = Seq(scalatest % "test", specs2 % "test")) settings(publish := ())
  
  // Dummy project to workaround https://github.com/harrah/xsbt/issues/85
  lazy val dummy = Project("Dummy", file("dummy"))
    
  val scalatestVersions = Map("2.8.1" -> "1.5.1", "2.9.1" -> "1.6.1")
  val specs2Versions = Map("2.8.1" -> "1.2", "2.9.1" -> "1.5")
    
  def scalatestVersion(scalaVersion: String) = getLibraryVersion(scalatestVersions, scalaVersion)
  def specs2Version(scalaVersion: String) = getLibraryVersion(specs2Versions, scalaVersion)
  
  def getLibraryVersion(versionMap: Map[String, String], scalaVersion: String) =
    versionMap.getOrElse(scalaVersion, error("Unsupported Scala version: "+ scalaVersion))
}
