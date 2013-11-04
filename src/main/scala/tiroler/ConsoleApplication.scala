package tiroler

import com.twitter.finagle.builder.Server


class ConsoleApplication extends Bootstrap {
  private var config:Config = _
  private var server:Server = _
  def init():ConsoleApplication = {
    config = loadConfigFromArgs(new Array[String](0))
    this
  }
  def start():ConsoleApplication = {
    server = new ApiServerFactory().build("daemon", config.port)
    this
  }
  def stop():ConsoleApplication = {
    server.close()
    this
  }
}

object ConsoleApplications extends App {
  new ConsoleApplication().init().start()
}