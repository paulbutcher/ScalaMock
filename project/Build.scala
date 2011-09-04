import sbt._
import Keys._

object BorachioBuild extends Build {
  
  override lazy val settings = super.settings ++ Seq(
    scalaVersion := "2.9.1",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "1.6.1" % "optional",
      "junit" % "junit" % "3.8.2" % "optional"
    )
  )
  
  lazy val library = Project("Library", file("library"))
}