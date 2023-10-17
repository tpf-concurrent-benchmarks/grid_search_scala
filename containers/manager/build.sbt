val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "manager",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "com.rabbitmq" % "amqp-client" % "5.14.2"
    )
  )
