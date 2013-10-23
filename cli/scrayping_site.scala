import com.ning.http.client.filter.{FilterContext, ResponseFilter}
import com.redis.RedisClient
import dispatch._, Defaults._
import java.io.StringReader
import java.sql.Timestamp
import nu.validator.htmlparser.common.XmlViolationPolicy
import nu.validator.htmlparser.sax.HtmlParser
import org.xml.sax.InputSource
import scala.collection.JavaConverters._
import scala.collection.mutable.HashMap
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.slick.session.Database
import scala.xml.parsing.NoBindingFactoryAdapter
import scala.xml.Text
import scala.slick.driver.MySQLDriver.simple._

object HttpClient {

  def fetchDataByXml(req: Req) = {
    val future:Future[String] = fetchData(req)
    @volatile var html= ""
    future onSuccess {
      case n => html += n
    }
    Await.result(future, Duration.Inf)

    val hp = new HtmlParser()
    hp.setNamePolicy(XmlViolationPolicy.ALLOW)

    val saxer = new NoBindingFactoryAdapter
    hp.setContentHandler(saxer)
    hp.parse(new InputSource(new StringReader(html)))

    val node = saxer.rootElem
    node
  }

  /**
   *
   * @param req
   * @return
   */
  def fetchData(req: Req):Future[String] = {
    val myHttp = Http.configure {
      builder =>
        builder.addResponseFilter(new ResponseFilter {
          override def filter(ctx: FilterContext[_]) = {
            ctx.getResponseHeaders.getHeaders.get("Content-Type").asScala.toList match {
              case "text/html" :: Nil =>
                ctx.getResponseHeaders.getHeaders.put("Content-Type", List("text/html; charset=utf-8").asJava)
              case _ => ()
            }
            ctx
          }
        })
    }
    val data = myHttp(req OK as.String)
    data
  }
}

case class Member(id: Int, firstName: String, lastName: String, instrumentId: Int)

object PointTotal extends Table[(Int, String, Int, Int, Timestamp)]("point_total") {
  def id = column[Int]("id", O PrimaryKey, O AutoInc)
  def code = column[String]("code", O DBType "varchar(10)")
  def point = column[Int]("point")
  def rank = column[Int]("rank")
  def time = column[Timestamp]("time")
  def * = id ~ code  ~ point ~ rank ~ time;
  // AutoIncrement なカラムを除外
  def ins = code  ~ point ~ rank ~ time;
}

val time = System.currentTimeMillis
val xml = HttpClient.fetchDataByXml(url("http://yomou.syosetu.com/rank/list/type/total_total/"))
var rank_list = Vector.empty[HashMap[String, String]]
val rank_h_list = xml \\ "div" filter (_ \ "@class" contains Text("rank_h"))
for (rank_h <- rank_h_list){
  var map = new HashMap[String,String]
  val rank_atag = rank_h.map(_ \\ "a" filter (_ \ "@class" contains Text("tl"))).head
  val rank_reg = "best(.*)".r
  val url_reg = "http://ncode.syosetu.com/(.*)/".r
  // rank
  (rank_atag \ "@id").head.text match {case rank_reg(n) => map += "rank" -> n}
  // id
  (rank_atag \ "@href").head.text match {case url_reg(n) => map += "id" -> n}
  // title
  map += "title"->rank_atag.text
  rank_list = map +: rank_list
}
rank_list = rank_list reverse
val rank_table_list = xml \\ "table" filter (_ \ "@class" contains Text("rank_table"))
var int = 0;
for (rank_table <- rank_table_list) {
  val rank_reg = "(.*)pt".r
  val pts = rank_table.map(_ \\ "span" filter (_ \ "@class" contains Text("attention")) head).text
    match {case rank_reg(n) => rank_list.updated(int, rank_list.apply(int) += ("point"-> n.replaceAll(",","")))}
  int = int+1
}
val redis = new RedisClient("192.168.11.110",6379)
val db = scala.slick.session.Database.forURL("jdbc:mysql://192.168.11.110:3306/narou", driver = "com.mysql.jdbc.Driver", user = "root")
if (redis.connect) {
  for (info <- rank_list) {
    val id = info.apply("id")
    val title = info.apply("title")
    val point = info.apply("point")
    val rank = info.apply("rank")
    // redis.del(id+"::name")
    // redis.del(id+"::point")
    // redis.del(id+"::rank")
    redis.set(id+"::name", title)
    redis.zadd(id+"::point", time, time + point)
    redis.zadd(id+"::rank", time, time + rank)
    // mysql> create table point_total (id bigint not null auto_increment, code varchar(10) not null, point int, rank int, time timestamp, primary key (id), key code_time (code,time);
    db withSession {implicit session: Session =>
      PointTotal.ins.insert(id,Integer.valueOf(point), Integer.valueOf(rank), new Timestamp(time))
    }
  }
}
print(rank_list)
