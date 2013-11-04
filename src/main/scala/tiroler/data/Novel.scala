package tiroler.data

class NovelDao {
  def populateAppInfo(list: List[String]):List[Map[String,Any]] = {
    var result:List[Map[String,Any]] = Nil
    val key_list = list map {s:String=> s+"::name"}
    val name_list:List[Option[String]] = NarouDB.redis.mget("",list map {s:String=> s+"::name"}:_*) get;
    list.zipWithIndex.foreach {
      case (e:String, i: Int) =>
        result = result :+ Map[String,Any](
          "id"->e,
          "name"->name_list.apply(i+1).get
      )
    }
    result
  }
}
