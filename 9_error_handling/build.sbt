name := "9_error_handling"

scalaVersion := "2.12.6"

scalacOptions ++= Seq("-deprecation")

libraryDependencies += "org.typelevel" %% "cats" % "0.9.0"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.1.0"

libraryDependencies += "org.typelevel" %% "cats-free" % "1.1.0"

libraryDependencies += "org.typelevel" %% "cats-effect" % "0.10"

libraryDependencies += "org.typelevel" %% "alleycats-core" % "1.1.0"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.23"
