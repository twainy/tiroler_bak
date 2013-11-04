package tiroler.service

import com.twitter.finagle.http.{Response, Request}
import com.twitter.finagle.Service
import scala.Predef.String
import com.twitter.util.Future
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.{HttpResponse, HttpResponseStatus, DefaultHttpResponse}
import org.jboss.netty.util.CharsetUtil
import tiroler.data.{NarouDB, PointTotalDao}
import scala.collection.mutable
import scala.slick.session.Session
import scala.util.parsing.json.{JSON, JSONArray, JSONObject}

object RankDataService extends Service[Request, Response]{
  def apply(request: Request): Future[Response] = Future {
    var nov_list:List[String] = List()
    request.getUri() split "/" match {
      case Array("","rank", novel_code_csv) => mutable.WrappedArray.make(novel_code_csv split ",") foreach {
        x:String => nov_list = x::nov_list
      }
    }
    val list = NarouDB.db withSession {implicit session:Session =>
      new PointTotalDao() selectByCodes(nov_list)
    }
    val json_array:JSONArray = JSONArray(list map {JSONObject(_)})
    val json_string = json_array.toString()
    new Response {
      def httpResponse: HttpResponse = {
        val res = new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)
        res.setContent(ChannelBuffers.copiedBuffer(json_string, CharsetUtil.UTF_8))
        res.setHeader("Access-Control-Allow-Origin","*")
        res
      }
    }
  }
}
