ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "common",
    idePackagePrefix := Some("org.grid_search.common")
  )

libraryDependencies += "com.github.andyglow" %% "typesafe-config-scala" % "2.0.0"
libraryDependencies += "org.scala-lang" %% "toolkit" % "0.1.7"

libraryDependencies += "com.newmotion" % "akka-rabbitmq_2.11" % "5.1.2"
libraryDependencies += "com.timgroup" % "java-statsd-client" % "3.0.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test

