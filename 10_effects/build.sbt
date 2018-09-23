name := "10_effects"

scalaVersion := "2.12.6"

scalacOptions ++= Seq("-deprecation")

val CatsVersion = "1.4.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % CatsVersion,
  "org.typelevel" %% "cats-free" % CatsVersion,
  "org.typelevel" %% "cats-effect" % "1.0.0",
  "org.typelevel" %% "alleycats-core" % CatsVersion,

  "org.scalaz" %% "scalaz-core" % "7.2.26",
  "org.scalaz" %% "scalaz-zio" % "0.2.7",

  "org.scalacheck" %% "scalacheck" % "1.13.5",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

val nexus = "https://oss.sonatype.org/"
resolvers += "sonatype snapshots" at nexus + "content/repositories/snapshots"
//resolvers += "sonatype releases" at nexus + "content/repositories/staging"
