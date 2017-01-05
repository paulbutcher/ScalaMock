import sbt._

object ShellPrompt {
  object devnull extends ProcessLogger {
    def info(s: => String): Unit = {}
    def error(s: => String): Unit = {}
    def buffer[T](f: => T): T = f
  }

  def currBranch: String = (
    ("git status -sb" lines_! devnull).headOption
      getOrElse "-" stripPrefix "## "
  )

  val buildShellPrompt: (State) => String = state => {
    val currProject = Project.extract(state).currentProject.id
    s"$currProject:$currBranch> "
  }
}
