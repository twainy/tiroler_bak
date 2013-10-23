package tiroler.response.http

import com.twitter.finagle.http.Response
import org.jboss.netty.handler.codec.http.{HttpVersion, HttpResponseStatus, HttpResponse, DefaultHttpResponse}
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.util.CharsetUtil
import scala.util.parsing.json.JSON

/**
 * Created with IntelliJ IDEA.
 * User: takeshi-wakasugi
 * Date: 2013/09/28
 * Time: 23:35
 * To change this template use File | Settings | File Templates.
 */
/*
class JSONResponse extends Response {
  def httpResponse: HttpResponse = {
    val res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
    res.setContent(ChannelBuffers.copiedBuffer(user_id, CharsetUtil.UTF_8))

  }

}
*/