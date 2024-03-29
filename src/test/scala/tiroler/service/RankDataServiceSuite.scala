package tiroler.service

import org.scalatest.FunSuite
import com.twitter.finagle.http.{Response, Version, Request}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.handler.codec.http
import org.jboss.netty.util.CharsetUtil
import scala.util.parsing.json.JSON

class RankDataServiceSuite extends FunSuite {
  test("get rank data") {
    val req = Request(new http.DefaultHttpRequest(Version.Http11, HttpMethod.GET, "/rank/n5166z,n4205bc"))
    //queryString
    val res = RankDataService(req)
    res onSuccess {res: Response =>
      val json_string = res.getContent.toString(CharsetUtil.UTF_8)
      val l = JSON.parseFull(json_string)
      l.get match {
        case s:List[Map[String,Any]] => s foreach {m:Map[String,Any] =>
          assert((m.get("code").get equals "n5166z") || (m.get("code").get equals "n4205bc"))
        }
      }
    }
  }
}
