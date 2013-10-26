package tiroler.service

import org.scalatest.FunSuite
import com.twitter.finagle.http.{Response, Version, Request}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.handler.codec.http
import org.jboss.netty.util.CharsetUtil

class RankDataServiceSuite extends FunSuite {
  test("get rank data") {
    val req = Request(new http.DefaultHttpRequest(Version.Http11, HttpMethod.GET, "/rank/n5166z,n4205bc"))
    //queryString
    val res = RankDataService(req)
    res onSuccess {res: Response =>
      res.getContent.toString(CharsetUtil.UTF_8)
    }
  }
}
