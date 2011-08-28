organization := "com.novus"

name := "rugu"

version := "0.1.0-SNAPSHOT"

sbtVersion := "0.10.1"

scalaVersion := "2.9.0-1"

crossScalaVersions := Seq("2.8.1", "2.9.0", "2.9.0-1")

libraryDependencies ++= Seq(
  "com.jcraft" % "jsch" % "0.1.44",
  //"org.scala-tools.testing" %% "specs" % "1.6.8"
  // TODO even with parallelExecution in Test := false, specs2 locks on remote tests
  "org.specs2" %% "specs2" % "1.5",
  // with Scala 2.8.1
  //"org.specs2" %% "specs2-scalaz-core" % "5.1-SNAPSHOT" % "test"
  // with Scala 2.9.0
  "org.specs2" %% "specs2-scalaz-core" % "6.0.RC2" % "test"
)

resolvers ++= Seq(
  "jsch" at " http://jsch.sf.net/maven2/",
  "snapshots" at "http://scala-tools.org/repo-snapshots",
  "releases" at "http://scala-tools.org/repo-releases"
)

initialCommands := "import com.novus.rugu._"

parallelExecution in Test := false
