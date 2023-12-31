import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "kafka-demo"
  )
  .dependsOn(
    pasha,
    google,
    linkedin,
    tinkoff,
    twitter
  )
  .aggregate(
    pasha,
    google,
    linkedin,
    tinkoff,
    twitter
  )

lazy val pasha = (project in file("services/pasha"))
  .settings(
    name := "project-pasha",
    libraryDependencies ++= Pasha.dependencies,
    scalacOptions ++= Seq("-Ymacro-annotations"),
  )
  .dependsOn(commonKafka)
  .aggregate(commonKafka)

lazy val google = (project in file("services/google"))
  .settings(
    name := "project-google",
    libraryDependencies ++= Google.dependencies,
    scalacOptions ++= Seq("-Ymacro-annotations"),
  )
  .dependsOn(commonKafka)
  .aggregate(commonKafka)

lazy val linkedin = (project in file("services/linkedin"))
  .settings(
    name := "project-linkedin",
    libraryDependencies ++= Linkedin.dependencies,
    scalacOptions ++= Seq("-Ymacro-annotations"),
  )
  .dependsOn(commonKafka)
  .aggregate(commonKafka)

lazy val tinkoff = (project in file("services/tinkoff"))
  .settings(
    name := "project-tinkoff",
    libraryDependencies ++= Tinkoff.dependencies,
    scalacOptions ++= Seq("-Ymacro-annotations"),
  )
  .dependsOn(commonKafka)
  .aggregate(commonKafka)

lazy val twitter = (project in file("services/twitter"))
  .settings(
    name := "project-twitter",
    libraryDependencies ++= Twitter.dependencies,
    scalacOptions ++= Seq("-Ymacro-annotations"),
  )
  .dependsOn(commonKafka)
  .aggregate(commonKafka)

lazy val commonKafka = (project in file("modules/kafka"))
  .settings(
    name := "project-kafka",
    libraryDependencies ++= Kafka.dependencies,
    scalacOptions ++= Seq("-Ymacro-annotations"),
  )
