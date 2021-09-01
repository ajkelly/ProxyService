name := "ProxyService"

version := "0.1"

scalaVersion := "2.12.4"

enablePlugins(AkkaGrpcPlugin)

libraryDependencies ++= {

  object v {
    val akkaHttp = "10.2.6"
    val akka = "2.6.9"
  }

  Seq(
    //testing
    "org.scalatest"           %% "scalatest"                        % "3.2.0"           % "test",
    "com.typesafe.akka"       %% "akka-stream-testkit"              % v.akka,
    "com.typesafe.akka"       %% "akka-http-testkit"                % v.akkaHttp,
    "com.stephenn"            %% "scalatest-json-jsonassert"        % "0.0.5"           % "test",
    //akka
    "com.typesafe.akka"       %% "akka-actor-typed"                 % v.akka,
    "com.typesafe.akka"       %% "akka-stream"                      % v.akka,
    "com.typesafe.akka"       %% "akka-actor"                       % v.akka,
    "com.typesafe.akka"       %% "akka-http"                        % v.akkaHttp,
    "com.typesafe.akka"       %% "akka-http-spray-json"             % v.akkaHttp
  )
}