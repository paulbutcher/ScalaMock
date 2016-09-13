import sbt.Keys._
import sbt.inc.Analysis

crossScalaVersions := Seq("2.11.8", "2.10.6")

val buildVersion = "3.3.0"

val buildScalaVersion = "2.11.8"

val buildSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "org.scalamock",
  version := buildVersion,
  scalaVersion := buildScalaVersion,
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
  scalacOptions in (Compile, doc) ++= Opts.doc.title("ScalaMock") ++ Opts.doc.version(buildVersion) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.sonatypeRepo("snapshots"),
  resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",

  publishTo <<= version { v =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomIncludeRepository := { _ => false },
  publishArtifact in Test := false,
  pomExtra := <url>http://scalamock.org/</url>
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
    </developers>,

  shellPrompt := ShellPrompt.buildShellPrompt
)


val specs2 = "org.specs2" %% "specs2" % "2.4.16"

// Specs2 and ScalaTest use different scala-xml versions
// and this caused problems with referencing class org.scalatest.events.Event
// val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.3" % "test"


lazy val core = crossProject.settings(buildSettings:_*)
  .in(file("core"))
  .settings(
    name := "ScalaMock Core",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )

lazy val jsCore = core.js

lazy val jvmCore = core.jvm  

lazy val scalatestSupport = crossProject.settings(buildSettings:_*)
  .in(file("frameworks/scalatest"))
  .settings(
    name := "ScalaMock ScalaTest Support",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.0"
  )
  .dependsOn(core)

lazy val jsScalatestSupport = scalatestSupport.js

lazy val jvmScalatestSupport = scalatestSupport.jvm

lazy val specs2Support = crossProject.settings(buildSettings:_*)
  .in(file("frameworks/specs2"))
  .settings(
    name := "ScalaMock Specs2 Support",
    libraryDependencies += specs2
  )
  .dependsOn(core)

lazy val jsSpecs2Support = specs2Support.js

lazy val jvmSpecs2Support = specs2Support.jvm

lazy val core_tests = crossProject.settings(buildSettings:_*)
  .in(file("core_tests"))
  .settings(
    name := "ScalaMock Core Tests",
    publish := (),
    publishLocal := ()
  )
  .dependsOn(scalatestSupport)

lazy val jscore_tests = core_tests.js

lazy val jvmcore_tests = core_tests.jvm  
  
lazy val examples = crossProject.settings(buildSettings:_*)
  .in(file("examples"))
  .settings(
    name := "ScalaMock Examples",
    publish := (),
    publishLocal := ()
  )
  .dependsOn(scalatestSupport, specs2Support)

lazy val jsExamples = examples.js

lazy val jvmExamples = examples.jvm

publishArtifact := false

publishArtifact in Test := false

publish := ()

publishLocal := ()
