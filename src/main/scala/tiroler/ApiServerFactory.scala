package tiroler

import java.lang.String
import java.net.InetSocketAddress
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http._
import com.twitter.finagle.http.path.{Path, Root, /}
import org.jboss.netty.handler.codec.http.HttpMethod
import com.twitter.finagle.Service
import com.twitter.finagle.http.RichHttp
import com.twitter.finagle.http.service.RoutingService
import tiroler.service.GraphDataService

/**
 * Created with IntelliJ IDEA.
 * User: takeshi-wakasugi
 * Date: 2013/09/24
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
class ApiServerFactory{

  private def byPathObject[REQUEST](routes: PartialFunction[(HttpMethod, Path), Service[REQUEST, Response]]) =
    new RoutingService(
      new PartialFunction[Request, Service[REQUEST, Response]] {
        def apply(request: Request) = routes((request.method, Path(request.path)))

        def isDefinedAt(request: Request) = routes.isDefinedAt((request.method, Path(request.path)))
      })

  private val routingService = byPathObject {
      case (Method.Get, Root / "graph") => GraphDataService // HandleExceptions andThen GraphDataService
    }
  def build(name: String, portOpt: Option[Int]): Server = {
    val server = ServerBuilder()
      .codec(RichHttp[Request](Http()))
      .bindTo(new InetSocketAddress(portOpt.getOrElse(8080)))
      .name(name)
      .build(routingService)
    server
  }}
