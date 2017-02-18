import sbt.Keys._

val project = Project(
  id = "demo-scala-multidb-slick-guice",
  base = file("."),
  settings = Seq(
    name := "demo-scala-multidb-slick-guice",
    scalaVersion := "2.11.8",

    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.1.1",
      "com.google.inject" % "guice" % "4.1.0",
      "net.codingwell" %% "scala-guice" % "4.0.1",

      "mysql" % "mysql-connector-java" % "6.0.5",
      "com.h2database" % "h2" % "1.4.193",
      "org.xerial" % "sqlite-jdbc" % "3.16.1",

      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    ),

    parallelExecution in Test := false
  )
)
