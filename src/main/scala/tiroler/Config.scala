package tiroler

import java.lang.String

trait Config {

    /** 環境状態(dev/production) */
    val state: Option[Int]

    /** API用ポート番号 */
    val apiPort: Option[Int]

}

