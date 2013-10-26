package tiroler.data

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 *
 */
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
