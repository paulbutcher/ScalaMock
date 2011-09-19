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

    libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
      val (scalatestVersion, specsVersion) = sv match {
        case "2.8.1" => ("1.5.1", "1.2")
        case "2.9.1" => ("1.6.1", "1.5")
        case _ => error("Unsupported Scala version:"+ sv)
      }
      deps ++ Seq(
        "org.scalatest" %% "scalatest" % scalatestVersion % "optional",
        "org.specs2" %% "specs2" % specsVersion % "optional",
        "junit" % "junit" % "3.8.2" % "optional")
    }
  )
  
  lazy val core = Project("Borachio core", file("core"))
}