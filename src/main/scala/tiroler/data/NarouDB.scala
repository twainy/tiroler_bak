package tiroler.data

import com.redis.RedisClient

object NarouDB {
  val db = scala.slick.session.Database.forURL("jdbc:mysql://192.168.11.110:3306/narou", driver = "com.mysql.jdbc.Driver", user = "root")
  val redis = new RedisClient("192.168.11.110",6379)
}
