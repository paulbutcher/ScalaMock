import sbtcrossproject.CrossPlugin.autoImport.crossProject

scalaVersion in ThisBuild := "2.11.12"
crossScalaVersions in ThisBuild := Seq("2.11.12", "2.12.8", "2.13.0")
//scalaJSUseRhino in ThisBuild := true

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.1.2"
lazy val specs2 = "org.specs2" %% "specs2-core" % "4.9.4"
lazy val scalameta = "org.scalameta" %% "scalameta" % "4.3.18"

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  unmanagedSourceDirectories in Compile ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2L, minor)) =>
        Some(baseDirectory.value.getParentFile / s"shared/src/main/scala-2.$minor")
      case _ =>
        None
    }
  },
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xcheckinit", "-target:jvm-1.8")
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
      scalameta,
      scalatest % Optional,
      specs2 % Optional
    )
  )

lazy val `scalamock-js` = scalamock.js
lazy val `scalamock-jvm` = scalamock.jvm

lazy val examples = project in file("examples") settings(
  commonSettings,
  name := "ScalaMock Examples",
  skip in publish := true,
  libraryDependencies ++= Seq(
    scalatest % Test,
    specs2 % Test
  )
) dependsOn scalamock.jvm
