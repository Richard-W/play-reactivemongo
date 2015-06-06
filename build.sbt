name := "play-reactivemongo"

organization := "xyz.wiedenhoeft"

version := "0.1-SNAPSHOT"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/richard-w/play-reactivemongo"),
    "scm:git:https://github.com/richard-w/play-reactivemongo",
    Some("scm:git:https://github.com/richard-w/play-reactivemongo")
  )
)

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "org.reactivemongo"       %% "reactivemongo"        % "0.10.5.0.akka23" % "compile",
  "com.typesafe.play"       %% "play"                 % "2.4.0"           % "provided",
  "org.scalatest"           %% "scalatest"            % "2.2.4"           % "test",
  "com.github.simplyscala"  %% "scalatest-embedmongo" % "0.2.2"           % "test"
)
