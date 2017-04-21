import sbt.inc.Analysis

scalaVersion in ThisBuild := "2.10.6"
crossScalaVersions in ThisBuild := Seq("2.10.6", "2.11.8", "2.12.1")
// the default in scala.js is now node.js, and rhino will be unsupported in v1.0
// update documentation to explain setup for this, then remove rhino for tests
scalaJSUseRhino in ThisBuild := true
organization in ThisBuild := "org.scalamock"
licenses in ThisBuild := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
scmInfo in ThisBuild := Some(
  ScmInfo(url("https://github.com/paulbutcher/ScalaMock"), "scm:git:git@github.com:paulbutcher/ScalaMock.git")
)
developers in ThisBuild := List(
  Developer("paulbutcher", "Paul Butcher", "", url("http://paulbutcher.com/")),
  Developer("backhorn", "Philipp Meyerhoefer", "", url("http://github.com/barkhorn"))
)
homepage in ThisBuild := Some(url("http://scalamock.org/"))

lazy val scalatest =  "org.scalatest" %% "scalatest" % "3.0.1"
lazy val specs2 = "org.specs2" %% "specs2-core" % "3.8.6"
lazy val scalaReflect = libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
lazy val quasiquotes = libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 10)) =>
      Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
        "org.scalamacros" %% "quasiquotes" % "2.1.0" cross CrossVersion.binary)
    case _ => Seq.empty
  }
}

// Specs2 and ScalaTest use different scala-xml versions
// and this caused problems with referencing class org.scalatest.events.Event
lazy val scalaXml = libraryDependencies ++= (
  if (scalaVersion.value < "2.11") Nil else Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.6" % Test)
)

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature",
    "-target:jvm-" + (if (scalaVersion.value < "2.11") "1.7" else "1.8")),
  scalacOptions in (Compile, doc) ++= Opts.doc.title("ScalaMock") ++ Opts.doc.version(version.value) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
  pomIncludeRepository := { _ => false },
  publishArtifact in Test := false,
  shellPrompt := ShellPrompt.buildShellPrompt//,
//  libraryDependencies ++= (
//    if (scalaVersion.value >= "2.11") Seq(compilerPlugin("ch.epfl.scala" %% "classpath-shrinker" % "0.1.1")) else Nil
//  )
)

lazy val `scalamock-core` = crossProject in file("core") settings(
    commonSettings,
    name := "ScalaMock Core",
    scalaReflect,
    quasiquotes
  )

lazy val `scalamock-core-js` = `scalamock-core`.js

lazy val `scalamock-core-jvm` = `scalamock-core`.jvm

lazy val `scalamock-scalatest-support` = crossProject in file("frameworks/scalatest") settings(
    commonSettings,
    name := "ScalaMock ScalaTest Support",
    libraryDependencies += scalatest,
    scalaXml
  ) dependsOn `scalamock-core`

lazy val `scalamock-scalatest-support-js` = `scalamock-scalatest-support`.js

lazy val `scalamock-scalatest-support-jvm` = `scalamock-scalatest-support`.jvm

lazy val `scalamock-specs2-support` = crossProject in file("frameworks/specs2") settings(
    commonSettings,
    name := "ScalaMock Specs2 Support",
    libraryDependencies += specs2
  ) dependsOn `scalamock-core`

lazy val `scalamock-specs2-support-js` = `scalamock-specs2-support`.js

lazy val `scalamock-specs2-support-jvm` = `scalamock-specs2-support`.jvm

lazy val core_tests = crossProject in file("core_tests") settings(
    commonSettings,
    name := "ScalaMock Core Tests",
    publish := (),
    publishLocal := ()
  ) dependsOn `scalamock-scalatest-support`
  
lazy val `core_tests-js` = core_tests.js

lazy val `core_tests-jvm` = core_tests.jvm

lazy val examples = crossProject in file("examples") settings(
    commonSettings,
    name := "ScalaMock Examples",
    publish := (),
    publishLocal := ()
  ) dependsOn(`scalamock-scalatest-support`, `scalamock-specs2-support`)

lazy val `examples-js` = examples.js

lazy val `examples-jvm` = examples.jvm

lazy val ScalaMock = crossProject in file(".") settings(
    commonSettings,
    publishArtifact in (Compile, packageBin) := false,
    publishArtifact in (Compile, packageSrc) := false,
    scalaReflect,
    quasiquotes,
    compile in Compile := Analysis.Empty,
    libraryDependencies ++= Seq(scalatest, specs2)
  ) aggregate(
    `scalamock-core`, core_tests, `scalamock-scalatest-support`, `scalamock-specs2-support`, examples
  ) jsSettings (
    sources in Compile := (
      (sources in Compile in `scalamock-core`.js).value
      ++ (sources in Compile in `scalamock-scalatest-support`.js).value
      ++ (sources in Compile in `scalamock-specs2-support`.js).value
    )
  ) jvmSettings (
    sources in Compile := (
      (sources in Compile in `scalamock-core`.jvm).value
      ++ (sources in Compile in `scalamock-scalatest-support`.jvm).value
      ++ (sources in Compile in `scalamock-specs2-support`.jvm).value
    )
  )

lazy val `ScalaMock-js` = ScalaMock.js

lazy val `ScalaMock-jvm` = ScalaMock.jvm

releaseCrossBuild := true
releaseProcess := {
  import ReleaseTransformations._
  Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeRelease", _)),
    pushChanges
  )
}


lazy val sonatypeCreds = for {
  u <- Option(System.getenv().get("SONATYPE_USERNAME"))
  p <- Option(System.getenv().get("SONATYPE_PASSWORD"))
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", u, p)

credentials ++= sonatypeCreds.toSeq
