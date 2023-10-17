ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "worker",
    idePackagePrefix := Some("org.grid_search.worker")
  )
libraryDependencies += "com.github.andyglow" %% "typesafe-config-scala" % "2.0.0"