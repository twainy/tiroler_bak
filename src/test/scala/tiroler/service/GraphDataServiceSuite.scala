package tiroler.service

import org.scalatest.FunSuite
import com.twitter.finagle.http.{Response, Version, Request}
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.handler.codec.http
import org.jboss.netty.util.CharsetUtil

/**
 * Created with IntelliJ IDEA.
 * User: takeshi-wakasugi
 * Date: 2013/09/28
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
 */
class GraphDataServiceSuite extends FunSuite {
  test("get graph data") {
    val req = Request(new http.DefaultHttpRequest(Version.Http11, HttpMethod.GET, "://?user_id=5432199"))
    //queryString
    val res = GraphDataService(req)
    res onSuccess {res: Response =>
      res.getContent.toString(CharsetUtil.UTF_8)
    }
  }
}
