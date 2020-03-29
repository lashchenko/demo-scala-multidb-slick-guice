import sbt.Keys._

val project = Project(
  id = "demo-scala-multidb-slick-guice",
  base = file("."),
  settings = Seq(
    name := "demo-scala-multidb-slick-guice",
    scalaVersion := "2.12.8",
    scalacOptions += "-deprecation",

    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.3.0",
      "com.google.inject" % "guice" % "4.2.1",
      "net.codingwell" %% "scala-guice" % "4.2.1",

      "mysql" % "mysql-connector-java" % "8.0.19",
      "com.h2database" % "h2" % "1.4.200",
      "org.xerial" % "sqlite-jdbc" % "3.30.1",

      "org.scalatest" %% "scalatest" % "3.1.1" % "test"
    ),

    parallelExecution in Test := false
  )
)
