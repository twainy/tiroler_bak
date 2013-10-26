package tiroler

import org.apache.commons.daemon.{DaemonContext, Daemon}
import com.twitter.finagle.builder.Server

/**
 * Created with IntelliJ IDEA.
 * User: takeshi-wakasugi
 * Date: 2013/09/24
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
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
    DaemonApplication.server = Some(new ApiServerFactory().build("daemon", config.apiPort))
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
