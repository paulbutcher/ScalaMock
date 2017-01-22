scalaVersion in ThisBuild := "2.10.6"
crossScalaVersions in ThisBuild := Seq("2.10.6", "2.11.8", "2.12.1")
// the default in scala.js is now node.js, and rhino will be unsupported in v1.0
// update documentation to explain setup for this, then remove rhino for tests
scalaJSUseRhino in ThisBuild := true
organization in ThisBuild := "org.scalamock"
licenses in ThisBuild := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
pomExtra in ThisBuild := {
  <url>http://scalamock.org/</url>
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
    </developers>
}

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
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
  scalacOptions in (Compile, doc) ++= Opts.doc.title("ScalaMock") ++ Opts.doc.version(version.value) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
  pomIncludeRepository := { _ => false },
  publishArtifact in Test := false,
  shellPrompt := ShellPrompt.buildShellPrompt
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
    libraryDependencies ++= Seq(scalatest, specs2)
  ) aggregate(
    `scalamock-core`, core_tests, `scalamock-scalatest-support`, `scalamock-specs2-support`, examples
  ) jsSettings (
    sources in Compile <<= Seq(`scalamock-core`, `scalamock-scalatest-support`, `scalamock-specs2-support`).map(sources in Compile in _.js).join.map(_.flatten)
  ) jvmSettings (
    sources in Compile <<= Seq(`scalamock-core`, `scalamock-scalatest-support`, `scalamock-specs2-support`).map(sources in Compile in _.jvm).join.map(_.flatten)
  )

lazy val `ScalaMock-js` = ScalaMock.js

lazy val `ScalaMock-jvm` = ScalaMock.jvm
