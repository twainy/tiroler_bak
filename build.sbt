name := "tiroler"

version := "1.0"

libraryDependencies ++= Seq("com.twitter" %% "finagle-http" % "6.2.0",
                       "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
                       "nu.validator.htmlparser" % "htmlparser" % "1.4",
                       "net.debasishg" %% "redisclient" % "2.10",
                       "com.typesafe" %% "slick" % "1.0.0-RC2",
                       "mysql" % "mysql-connector-java" % "5.1.26"
                       )
