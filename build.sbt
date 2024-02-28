import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val scalatest = Def.setting("org.scalatest" %%% "scalatest" % "3.2.17")
lazy val specs2 = Def.setting("org.specs2" %%% "specs2-core" % "4.20.2")

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  /**
   *  Symbol.newClass is marked experimental, so we should use @experimental annotation in every test suite.
   *  3.3.0 has a bug so we can omit this annotation
   */
  scalaVersion := "3.3.0",
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-release:8")
)

lazy val scalamock = crossProject(JSPlatform, JVMPlatform) in file(".") settings(
    commonSettings,
    crossScalaSettings,
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
    )
  )

lazy val examples = project in file("examples") settings(
  commonSettings,
  crossScalaSettings,
  name := "ScalaMock Examples",
  publish / skip := true,
  libraryDependencies ++= Seq(
    scalatest.value % Test,
    specs2.value % Test
  )
) dependsOn scalamock.jvm

def crossScalaSettings = {
  def addDirsByScalaVersion(path: String): Def.Initialize[Seq[sbt.File]] =
    scalaVersion.zip(baseDirectory) { case (v, base) =>
      CrossVersion.partialVersion(v) match {
        case Some((v, _)) if Set(2L, 3L).contains(v) =>
          Seq(base / path / s"scala-$v")
        case _ =>
          Seq.empty
      }
    }
  Seq(
    crossScalaVersions := Seq("2.12.18", "2.13.13", scalaVersion.value),
    Compile / unmanagedSourceDirectories ++= addDirsByScalaVersion("src/main").value,
    Test / unmanagedSourceDirectories ++= addDirsByScalaVersion("src/test").value,
    libraryDependencies ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, _)) =>
          Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value)
        case _ =>
          Seq.empty
      }
    }
  )
}