package tiroler

import java.lang.String

trait Config {

    /** 環境 dev|production */
    val state: Option[Int]

    /** ポート番号 */
    val port: Option[Int]

}

