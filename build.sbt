scalaVersion in ThisBuild := "2.10.6"
crossScalaVersions in ThisBuild := Seq("2.10.6", "2.11.11", "2.12.3", "2.13.0-M1")
scalaJSUseRhino in ThisBuild := true
organization in ThisBuild := "org.scalamock"
licenses in ThisBuild := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
scmInfo in ThisBuild := Some(
  ScmInfo(url("https://github.com/paulbutcher/ScalaMock"), "scm:git:git@github.com:paulbutcher/ScalaMock.git")
)
developers in ThisBuild := List(
  Developer("paulbutcher", "Paul Butcher", "", url("http://paulbutcher.com/")),
  Developer("barkhorn", "Philipp Meyerhoefer", "", url("https://github.com/barkhorn"))
)
homepage in ThisBuild := Some(url("http://scalamock.org/"))

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.3"
lazy val specs2 = "org.specs2" %% "specs2-core" % "3.9.1"
lazy val quasiquotes = libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 10)) =>
      Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
        "org.scalamacros" %% "quasiquotes" % "2.1.0" cross CrossVersion.binary)
    case _ => Seq.empty
  }
}

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xlint",
    "-target:jvm-" + (if (scalaVersion.value < "2.11") "1.7" else "1.8"))
)

lazy val examples = crossProject in file("examples") settings(
    commonSettings,
    name := "ScalaMock Examples",
    publishArtifact := false,
    libraryDependencies ++= Seq(
      scalatest % Test,
      specs2 % Test
    )
  ) dependsOn scalamock

lazy val `examples-js` = examples.js

lazy val `examples-jvm` = examples.jvm

lazy val scalamock = crossProject in file(".") settings(
    commonSettings,
    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    ),
    quasiquotes,
    name := "scalamock",
    publishArtifact in (Compile, packageBin) := true,
    publishArtifact in (Compile, packageDoc) := true,
    publishArtifact in (Compile, packageSrc) := true,
    publishArtifact in Test := false,
    scalacOptions in (Compile, doc) ++= Opts.doc.title("ScalaMock") ++ Opts.doc.version(version.value) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
    pomIncludeRepository := { _ => false },
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      scalatest % Optional,
      specs2 % Optional
    )
  )

lazy val `scalamock-js` = scalamock.js

lazy val `scalamock-jvm` = scalamock.jvm

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
    releaseStepCommand("publishSigned")
  )
}

credentials ++= (
  for {
    u <- Option(System.getenv().get("SONATYPE_USERNAME"))
    p <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", u, p)
).toSeq

{
  val f = Path.userHome / ".sbt" / ".gpgsettings"
  if (f.exists) {
    println(s"pgp settings loaded from $f")
    val pphrase :: hexkey :: _ = IO.readLines(f)
    usePgpKeyHex(hexkey)
    Seq(
      pgpPassphrase := Some(pphrase.toCharArray),
      useGpg := true
    )
  } else {
    println(s"$f does not exist - pgp settings empty")
    Seq.empty[Def.Setting[_]]
  }
}

version in ThisBuild := {
  val Snapshot = """(\d+)\.(\d+)\.(\d+)-\d+.*?""".r
  git.gitDescribedVersion.value.getOrElse("0.0.0-1")match {
    case Snapshot(maj, min, pat) => s"$maj.${min.toInt + 1}.$pat-SNAPSHOT"
    case v => v
  }
}

isSnapshot in ThisBuild := version.value.endsWith("-SNAPSHOT")
