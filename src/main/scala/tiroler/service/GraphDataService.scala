package tiroler.service

import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.Service
import scala.Predef.String
import com.twitter.util.Future
import com.twitter.concurrent.Broker
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBuffer}
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpResponseStatus, DefaultHttpResponse}
import org.jboss.netty.util.CharsetUtil

/**
 * Created with IntelliJ IDEA.
 * User: takeshi-wakasugi
 * Date: 2013/09/25
 * Time: 0:13
 * To change this template use File | Settings | File Templates.
 */
object GraphDataService extends Service[Request, Response]{
  private val addBroker = new Broker[(String, Broker[ChannelBuffer])]
  private val remBroker = new Broker[(String, Broker[ChannelBuffer])]
  def apply(request: Request): Future[Response] = Future {
    val requestProxy = Request(request)
    val userId = requestProxy.getParam("userId")
    val subscriber = new Broker[ChannelBuffer]
    addBroker !(userId, subscriber)

    val user_id = request.getParam("user_id")

    new Response {
      def httpResponse: HttpResponse = {
        val res = new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)
        res.setContent(ChannelBuffers.copiedBuffer(user_id, CharsetUtil.UTF_8))
        res
      }
    }
  }
}
