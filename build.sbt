name := "grid-search-scala"

version := "1.0"

scalaVersion := "2.13.12"
val AkkaVersion = "2.8.4"
val CassandraPluginVersion = "1.1.1"
val AkkaDiagnosticsVersion = "2.0.0"
val LogbackClassicVersion = "1.2.11"
val ScalaTestVersion = "3.1.1"
val CommonIoVersion = "2.4"

run / fork := true

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-cluster-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-persistence-query" % AkkaVersion,
    "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
    "com.typesafe.akka" %% "akka-persistence-cassandra" % CassandraPluginVersion,
    "ch.qos.logback" % "logback-classic" % LogbackClassicVersion,
    "com.lightbend.akka" %% "akka-diagnostics" % AkkaDiagnosticsVersion,
    // test dependencies
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
    "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,
    "commons-io" % "commons-io" % CommonIoVersion % Test)
