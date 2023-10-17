ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "manager",
    idePackagePrefix := Some("org.grid_search.manager")
  )
libraryDependencies += "com.github.andyglow" %% "typesafe-config-scala" % "2.0.0"
libraryDependencies += "com.newmotion" % "akka-rabbitmq_2.11" % "5.1.2"