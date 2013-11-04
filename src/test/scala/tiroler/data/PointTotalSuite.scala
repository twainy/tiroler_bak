package tiroler

import tiroler.data.{NarouDB, PointTotalDao}
import org.scalatest.FunSuite
import scala.slick.session.Session

class PointTotalSuite extends FunSuite {

  test("query select") {
    NarouDB.db withSession {implicit session: Session =>
      val l = new PointTotalDao().selectAll()
      assert(l.length > 0)
      val q = new PointTotalDao().selectByCodes("n9669bk"::Nil)
      assert(q.length > 0)
    }
  }
  test("get code list") {
    NarouDB.db withSession {implicit session: Session =>
      val l = new PointTotalDao().getNovelCodeList()
      assert(l.length > 100)
    }
  }

}
