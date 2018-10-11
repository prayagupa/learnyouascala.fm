name := "fp-types"

scalaVersion := "2.12.7"

scalacOptions ++= Seq("-deprecation", "-Ypartial-unification")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.typelevel" %% "cats-core" % "1.4.0",

  "junit" % "junit" % "4.10" % "test",
  "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test

)
