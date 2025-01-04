import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val scalatest = Def.setting("org.scalatest" %%% "scalatest" % "3.2.19")
lazy val specs2 = Def.setting("org.specs2" %%% "specs2-core" % "4.20.8")

val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  scalaVersion := "3.6.2",
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-experimental"
  )
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
    )
  )

lazy val examples = project in file("examples") settings(
  commonSettings,
  name := "ScalaMock Examples",
  publish / skip := true,
  libraryDependencies ++= Seq(
    scalatest.value % Test,
    specs2.value % Test
  )
) dependsOn scalamock.jvm

inThisBuild(
  List(
    organization := "org.scalamock",
    homepage := Some(url("http://scalamock.org/")),
    licenses := List(
      "MIT" -> url("https://opensource.org/licenses/MIT")
    ),
    developers := List(
      Developer("paulbutcher", "Paul Butcher", "", url("http://paulbutcher.com/")),
      Developer("barkhorn", "Philipp Meyerhoefer", "", url("https://github.com/barkhorn")),
      Developer("goshacodes", "Georgii Kovalev", "", url("https://github.com/goshacodes"))
    )
  )
)
