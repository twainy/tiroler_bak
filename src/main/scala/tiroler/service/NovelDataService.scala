package tiroler.service

import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.Service
import com.twitter.util.Future
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpResponseStatus, DefaultHttpResponse}
import org.jboss.netty.util.CharsetUtil
import tiroler.data.{NovelDao, NarouDB, PointTotalDao}
import scala.slick.session.Session
import scala.util.parsing.json.{JSON, JSONArray, JSONObject}

object NovelDataService extends Service[Request, Response]{
  def apply(request: Request): Future[Response] = Future {
    val code_list = NarouDB.db withSession {implicit session:Session =>
      new PointTotalDao() getNovelCodeList()
    }
    val list = new NovelDao() populateAppInfo(code_list)
    val json_array:JSONArray = JSONArray(list map {JSONObject(_)})
    val json_string = json_array.toString()
    new Response {
      def httpResponse: HttpResponse = {
        val res = new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)
        res.setHeader("Access-Control-Allow-Origin","*")
        res.setContent(ChannelBuffers.copiedBuffer(json_string, CharsetUtil.UTF_8))
        res
      }
    }
  }
}
