name := "notification-service"
organization := "org.perseus"
version := "0.0.1"
scalaVersion := "2.12.6"
logLevel := Level.Debug

resolvers += Resolver.jcenterRepo

Revolver.enableDebugging(port = 5005, suspend = false)

enablePlugins(DockerPlugin)

dockerfile in docker := {
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}

val akkaHttp = "10.1.1"
val akka = "2.5.11"
val circe = "0.9.3"
val json4sVersion = "3.6.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "com.typesafe.akka" %% "akka-stream" % akka,
  "com.typesafe.akka" %% "akka-slf4j" % akka,
  "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",
  "io.circe" %% "circe-core" % "0.11.1",
  "io.circe" %% "circe-generic" % "0.11.1",
  "io.circe" %% "circe-parser" % "0.11.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
  
  "net.softler" %% "akka-http-rest-client" % "0.2.1",

  //test libraries
  "org.scalamock" %% "scalamock" % "4.1.0" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test",
  "org.pegdown" % "pegdown" % "1.6.0" % "test"
)

testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)