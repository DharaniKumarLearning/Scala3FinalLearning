 ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "Scala3FinalLearning"
  )

libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3"
libraryDependencies += "org.flywaydb" % "flyway-database-postgresql" % "11.4.1"
libraryDependencies += "org.postgresql" % "postgresql" % "42.7.3"
libraryDependencies += "com.zaxxer" % "HikariCP" % "5.1.0"

libraryDependencies ++= Seq(
   "org.scalatest" %% "scalatest" % "3.2.18" % Test,
   "com.h2database" % "h2" % "2.2.224" % Test
)