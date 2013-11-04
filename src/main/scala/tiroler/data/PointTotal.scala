package tiroler.data

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.StaticQuery

class PointTotalDao {
  case class PointTotal(id: Int, code: String, point: Int, rank: Int, time: Timestamp)
  object PointTotals extends Table[PointTotal]("point_total") {
    def id = column[Int]("id", O PrimaryKey, O AutoInc)
    def code = column[String]("code", O DBType "varchar(10)")
    def point = column[Int]("point")
    def rank = column[Int]("rank")
    def time = column[Timestamp]("time")
    def * = id ~ code ~ point ~ rank ~ time <> (PointTotal, PointTotal.unapply _)
    // AutoIncrement なカラムを除外
    def ins = code  ~ point ~ rank ~ time;
  }
  def toMapList (list: List[PointTotal]):List[Map[String,Any]] = {
    list map {p:PointTotal => toMap(p)}
  }
  def toMap (p: PointTotal):Map[String,Any] = {
    Map(
      "id"->p.id,
      "code"->p.code,
      "rank"->p.rank,
      "point"->p.point,
      "time"->p.time.getTime
    )
  }
  def selectByCodes (codes: List[String], offset: Int = 0)(implicit session: Session):List[Map[String,Any]] = {
    val list =
      if (offset == 0)
        (for { p <- PointTotals if p.code inSet codes} yield p) list()
      else
        (for { p <- PointTotals if p.code inSet codes} yield p) take(offset) list()
    toMapList(list)
  }
  def selectAll (offset: Int = 10)(implicit session: Session):List[Map[String,Any]] = {
    val list = (for { p <- PointTotals} yield p) take(offset) list()
    toMapList(list)
  }
  def getNovelCodeList ()(implicit session:Session):List[String] = {
    val list = StaticQuery.queryNA[String]("select distinct(code) from point_total") list()
    list
  }
}
