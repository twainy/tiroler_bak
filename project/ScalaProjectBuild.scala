import sbt._
import sbt.Keys._

object ScalaProjectBuild extends Build {

  lazy val scalaProject = Project(
    id = "scala-project",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Scala Project",
      organization := "org.example",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2"
      // add other settings here
    )
  )
}
