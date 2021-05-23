import sbtcrossproject.CrossPlugin.autoImport.crossProject

scalaVersion in ThisBuild := "2.11.12"
crossScalaVersions in ThisBuild := Seq("2.11.12", "2.12.13", "2.13.6")
//scalaJSUseRhino in ThisBuild := true

lazy val scalatest = Def.setting("org.scalatest" %%% "scalatest" % "3.2.9")
lazy val specs2 = Def.setting("org.specs2" %%% "specs2-core" % "4.10.6")

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
      scalatest.value % Optional,
      specs2.value % Optional
    )
  )

lazy val `scalamock-js` = scalamock.js
lazy val `scalamock-jvm` = scalamock.jvm

lazy val examples = project in file("examples") settings(
  commonSettings,
  name := "ScalaMock Examples",
  skip in publish := true,
  libraryDependencies ++= Seq(
    scalatest.value % Test,
    specs2.value % Test
  )
) dependsOn scalamock.jvm
