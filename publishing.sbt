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
pomIncludeRepository in ThisBuild := { _ => false }

version in ThisBuild := {
  val Snapshot = """(\d+)\.(\d+)\.(\d+)-\d+.*?""".r
  git.gitDescribedVersion.value.getOrElse("0.0.0-1")match {
    case Snapshot(maj, min, _) => s"$maj.${min.toInt + 1}.0-SNAPSHOT"
    case v => v
  }
}

isSnapshot in ThisBuild := version.value.endsWith("-SNAPSHOT")

publishTo in ThisBuild := Some(
  if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging
)
publishConfiguration in ThisBuild := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration in ThisBuild := publishLocalConfiguration.value.withOverwrite(true)

addCommandAlias("ci-all",  ";+clean ;+compile ;+test ;+package")
addCommandAlias("release", ";+publishSigned ;sonatypeReleaseAll")

credentials ++= (
  for {
    u <- Option(System.getenv().get("SONATYPE_USERNAME"))
    p <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", u, p)
  ).toSeq
