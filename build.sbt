name := "shard"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.jsoup" % "jsoup" % "1.9.2",
  "ch.qos.logback" %  "logback-classic" % "1.1.7",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.github.scopt" %% "scopt" % "3.5.0",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
  "io.spray" %%  "spray-json" % "1.3.2"
)

resolvers += Resolver.sonatypeRepo("public")

mainClass in Compile := Some("fish.philwants.Runner")