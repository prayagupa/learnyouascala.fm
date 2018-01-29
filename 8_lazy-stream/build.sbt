name := course.value + "-" + assignment.value

scalaVersion := "2.12.4"

scalacOptions ++= Seq("-deprecation")

// grading libraries
libraryDependencies += "junit" % "junit" % "4.10" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test

// include the common dir
commonSourcePackages += "common"

courseId := "PeZYFz-zEeWB_AoW1KYI4Q"
