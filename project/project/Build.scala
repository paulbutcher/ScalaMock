import sbt._
object PluginDef extends Build {
	override lazy val projects = Seq(root)
	lazy val root = Project("plugins", file(".")) dependsOn(scalamockPlugin)
  lazy val scalamockPlugin = uri("git://github.com/paulbutcher/scalamock-sbt-plugin")
}