ThisBuild / organization := "org.scalamock"
ThisBuild / licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/paulbutcher/ScalaMock"), "scm:git:git@github.com:paulbutcher/ScalaMock.git")
)
ThisBuild / developers := List(
  Developer("paulbutcher", "Paul Butcher", "", url("http://paulbutcher.com/")),
  Developer("barkhorn", "Philipp Meyerhoefer", "", url("https://github.com/barkhorn"))
)
ThisBuild / homepage := Some(url("http://scalamock.org/"))
//ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / version := {
  val Snapshot = """(\d+)\.(\d+)\.(\d+)-\d+.*?""".r
  git.gitDescribedVersion.value.getOrElse("0.0.0-1")match {
    case Snapshot(maj, min, _) => s"$maj.${min.toInt + 1}.0-SNAPSHOT"
    case v => v
  }
}

ThisBuild / isSnapshot := version.value.endsWith("-SNAPSHOT")

ThisBuild / publishTo := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
ThisBuild / publishConfiguration := publishConfiguration.value.withOverwrite(true)
ThisBuild / publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

addCommandAlias("ci-all",  ";+clean ;+compile ;+test ;+package")
addCommandAlias("release", ";+scalamockJVM/publishSigned ;+scalamockJS/publishSigned ;sonatypeReleaseAll")

credentials ++= (
  for {
    u <- Option(System.getenv().get("SONATYPE_USERNAME"))
    p <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", u, p)
  ).toSeq
