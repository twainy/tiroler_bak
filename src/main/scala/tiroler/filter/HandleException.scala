package tiroler.filter

import com.twitter.finagle.{Service, SimpleFilter}
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.util.CharsetUtil

/**
 * Created with IntelliJ IDEA.
 * User: takeshi-wakasugi
 * Date: 2013/09/24
 * Time: 23:38
 * To change this template use File | Settings | File Templates.
 */
object HandleExceptions extends SimpleFilter[HttpRequest, HttpResponse] {
  def apply(request: HttpRequest, service: Service[HttpRequest, HttpResponse]) = {
    // `handle` asynchronously handles exceptions.
    service(request) handle {
      case error =>
        val statusCode = error match {
          case _: IllegalArgumentException =>
            HttpResponseStatus.FORBIDDEN
          case _ =>
            HttpResponseStatus.INTERNAL_SERVER_ERROR
        }
        val errorResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, statusCode)
        errorResponse.setContent(ChannelBuffers.copiedBuffer(error.getStackTraceString, CharsetUtil.UTF_8))

        errorResponse
    }
  }
}

