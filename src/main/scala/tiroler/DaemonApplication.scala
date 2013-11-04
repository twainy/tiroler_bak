package tiroler

import org.apache.commons.daemon.{DaemonContext, Daemon}
import com.twitter.finagle.builder.Server

/**
 * デーモンアプリケーション。
 */
class DaemonApplication extends Daemon with Bootstrap {

  private var config: Config = _

  def init(context: DaemonContext) {
    DaemonApplication.daemonContext = context
    val args = context.getArguments
    config = loadConfigFromArgs(args)
  }

  def start() {
    DaemonApplication.server = Some(new ApiServerFactory().build("daemon", config.port))
  }

  def stop() {
    DaemonApplication.server.foreach(_.close())
  }

  def destroy() {

  }

}

object DaemonApplication {

  @volatile
  private var daemonContext: DaemonContext = _

  @volatile
  private var server: Option[Server] = None

  def getDaemonContext: DaemonContext = daemonContext

}
