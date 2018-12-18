import sbtcrossproject.CrossPlugin.autoImport.crossProject

scalaVersion in ThisBuild := "2.11.12"
crossScalaVersions in ThisBuild := Seq("2.10.7", "2.11.12", "2.12.6", "2.13.0-M3")
scalaJSUseRhino in ThisBuild := true

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.5-M1"
lazy val specs2 = Def.setting {
  val v = CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 10)) => "3.9.1" // specs2 4.x does not support Scala 2.10
    case _ => "4.0.2"
  }
  "org.specs2" %% "specs2-core" % v
}

lazy val withQuasiquotes = libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 10)) =>
      Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
        "org.scalamacros" %% "quasiquotes" % "2.1.0" cross CrossVersion.binary)
    case _ => Seq.empty
  }
}

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  unmanagedSourceDirectories in Compile ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2L, minor)) =>
        Some(baseDirectory.value.getParentFile / s"shared/src/main/scala-2.$minor")
      case _ =>
        None
    }
  },
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xcheckinit",
    "-target:jvm-" + (if (scalaVersion.value < "2.11") "1.7" else "1.8"))
)

lazy val scalamock = crossProject(JSPlatform, JVMPlatform) in file(".") settings(
    commonSettings,
    name := "scalamock",
    publishArtifact in (Compile, packageBin) := true,
    publishArtifact in (Compile, packageDoc) := true,
    publishArtifact in (Compile, packageSrc) := true,
    publishArtifact in Test := false,
    scalacOptions in (Compile, doc) ++= Opts.doc.title("ScalaMock") ++ 
      Opts.doc.version(version.value) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      scalatest % Optional,
      specs2.value % Optional
    ),
    withQuasiquotes
  )

lazy val `scalamock-js` = scalamock.js
lazy val `scalamock-jvm` = scalamock.jvm

lazy val examples = crossProject(JSPlatform, JVMPlatform) in file("examples") settings(
  commonSettings,
  name := "ScalaMock Examples",
  skip in publish := true,
  libraryDependencies ++= Seq(
    scalatest % Test,
    specs2.value % Test
  )
) dependsOn scalamock

lazy val `examples-js` = examples.js
lazy val `examples-jvm` = examples.jvm
