package tiroler

import java.lang.String
import java.io.File
import com.twitter.util.Eval

/**
 * 起動処理を行うクラスが利用すべきTrait
 */
trait Bootstrap {
  private val DefaultConfigPath = "etc/tiroler.conf"

  protected def loadConfigFromArgs(args: Array[String]): Config = {
    val configPath = args.toList match {
      case Nil => DefaultConfigPath
      case headArg :: _ => headArg
    }
    new Eval().apply[Config](new File(configPath))
  }
}
