import sbtcrossproject.CrossPlugin.autoImport.crossProject

ThisBuild / scalaVersion := "2.11.12"
ThisBuild / crossScalaVersions := Seq("2.11.12", "2.12.15", "2.13.7", "3.0.1")
//ThisBuild / scalaJSUseRhino := true

lazy val scalatest = Def.setting("org.scalatest" %%% "scalatest" % "3.2.10")
lazy val specs2 = Def.setting("org.specs2" %%% "specs2-core" % "5.0.0-RC-22")

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  Compile / unmanagedSourceDirectories ++= {
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
    Compile / packageBin / publishArtifact := true,
    Compile / packageDoc / publishArtifact := true,
    Compile / packageSrc / publishArtifact := true,
    Test / publishArtifact := false,
    Compile / doc / scalacOptions ++= Opts.doc.title("ScalaMock") ++
      Opts.doc.version(version.value) ++ Seq("-doc-root-content", "rootdoc.txt", "-version"),
    libraryDependencies ++= Seq(
      scalatest.value % Optional,
      specs2.value % Optional
    ),
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2L, minor)) =>
          Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value, specs2.value % Optional)
        case _ => Nil
      }
    }
  )

lazy val `scalamock-js` = scalamock.js
lazy val `scalamock-jvm` = scalamock.jvm

lazy val examples = project in file("examples") settings(
  commonSettings,
  name := "ScalaMock Examples",
  publish / skip := true,
  libraryDependencies ++= Seq(
    scalatest.value % Test,
    specs2.value % Test
  )
) dependsOn scalamock.jvm
