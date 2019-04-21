name := """commenter-play-api"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

libraryDependencies += "org.springframework.security" % "spring-security-web" % "5.1.5.RELEASE"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "8.0.15",
  "org.scalikejdbc" %% "scalikejdbc" % "3.3.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

// https://mvnrepository.com/artifact/de.huxhorn.sulky/de.huxhorn.sulky.ulid
libraryDependencies += "de.huxhorn.sulky" % "de.huxhorn.sulky.ulid" % "8.2.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
