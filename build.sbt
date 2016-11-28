scalaVersion in ThisBuild := "2.10.6"
crossScalaVersions in ThisBuild := Seq("2.10.6", "2.11.8", "2.12.0")

lazy val scalatest =  "org.scalatest" %% "scalatest" % "3.0.1"
lazy val specs2 = "org.specs2" %% "specs2-core" % "3.8.6"
lazy val scalaReflect = libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

// Specs2 and ScalaTest use different scala-xml versions
// and this caused problems with referencing class org.scalatest.events.Event
lazy val scalaXml = libraryDependencies ++= (
  if (scalaVersion.value < "2.11") Nil else Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.6" % "test")
)

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "org.scalamock",
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
  scalacOptions in (Compile, doc) ++= Opts.doc.title("ScalaMock") ++ Opts.doc.version(version.value) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
  pomIncludeRepository := { _ => false },
  publishArtifact in Test := false,
  licenses := Seq("BSD" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  pomExtra := (
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
    </developers>),
  
  shellPrompt := ShellPrompt.buildShellPrompt
)

lazy val `scalamock-core` = project in file("core") settings(
    name := "ScalaMock Core",
    commonSettings,
    scalaReflect
  )

lazy val `scalamock-scalatest-support` = project in file("frameworks/scalatest") settings(
    name := "ScalaMock ScalaTest Support",
    commonSettings,
    libraryDependencies += scalatest,
    scalaXml
  ) dependsOn `scalamock-core`

lazy val `scalamock-specs2-support` = project in file("frameworks/specs2") settings(
    name := "ScalaMock Specs2 Support",
    commonSettings,
    libraryDependencies += specs2
  ) dependsOn `scalamock-core`

lazy val core_tests = project in file("core_tests") settings(
    name := "ScalaMock Core Tests",
    commonSettings,
    publish := (),
    publishLocal := ()
  ) dependsOn `scalamock-scalatest-support`
  
lazy val examples = project in file("examples") settings(
    name := "ScalaMock Examples",
    commonSettings,
    publish := (),
    publishLocal := ()
  ) dependsOn(`scalamock-scalatest-support`, `scalamock-specs2-support`)

lazy val ScalaMock = project in file(".") settings(
    commonSettings,
    publishArtifact in (Compile, packageBin) := false,
    publishArtifact in (Compile, packageSrc) := false,
    scalaReflect,
    libraryDependencies += scalatest,
    sources in Compile <<= Seq(`scalamock-core`, `scalamock-scalatest-support`).map(sources in Compile in _).join.map(_.flatten)
  ) aggregate(`scalamock-core`, core_tests, `scalamock-scalatest-support`, `scalamock-specs2-support`, examples)
