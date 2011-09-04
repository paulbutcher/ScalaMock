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

object BorachioBuild extends Build {
  
  override lazy val settings = super.settings ++ Seq(
      organization := "com.borachio",
      version := "2.0-SNAPSHOT",
      scalaVersion := "2.9.1",
      scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings"),
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "1.6.1" % "optional",
        "junit" % "junit" % "3.8.2" % "optional"
      )
    )
    
  lazy val borachio = Project(
      "Borachio",
      file(".")
    ) aggregate(library, compiler_plugin, compiler_plugin_tests)
  
  lazy val library = Project(
      "Library", 
      file("library")
    )

  lazy val compiler_plugin = Project(
      "CompilerPlugin", 
      file("compiler_plugin")
    ) settings(
      libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.9.1"
    ) dependsOn(library)
}
