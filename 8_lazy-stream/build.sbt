name := "lazy-stream"

scalaVersion := "2.12.7"

scalacOptions ++= Seq("-deprecation")

libraryDependencies += "org.typelevel" %% "cats-core" % "1.1.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"
libraryDependencies += "org.mongodb" %% "casbah" % "3.1.1"

libraryDependencies += "junit" % "junit" % "4.10" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test
