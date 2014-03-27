import sbt._

import sbt.Keys._

name := "kamon-akka-example"
 
version := "1.0"
 
scalaVersion := "2.10.2"

resolvers += "Kamon repo" at "http://repo.kamon.io"

libraryDependencies ++= Seq(
    "kamon"                         % "kamon-core"              % "0.0.14",
    "com.typesafe.akka"             %%  "akka-slf4j"            % "2.2.3",
    "org.slf4j"                     %   "slf4j-api"             % "1.7.5",
    "ch.qos.logback"                %   "logback-classic"       % "1.0.13",
    "org.aspectj"                   %   "aspectjweaver"         % "1.7.4"
)

fork in run := true

//You must provide the absolut path to the aspectjweaver in order to make it work within SBT
//If you are going to run this outside sbt, you should pass -javaagent with the aspectjweaver to java. 
javaOptions in run += "-javaagent:/home/smagnacco/.ivy2/cache/org.aspectj/aspectjweaver/jars/aspectjweaver-1.7.4.jar"
