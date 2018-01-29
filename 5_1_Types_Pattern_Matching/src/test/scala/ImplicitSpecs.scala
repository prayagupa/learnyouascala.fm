import org.scalatest.{FunSuite, Matchers}

import scala.util.{Success, Try}

class ImplicitSpecs extends FunSuite with Matchers {

  def process1()(implicit data: String): Option[String] = {
    Some(data + "-status1")
  }

  def process2()(implicit data: String): Option[String] = {
    Some(data + "-status2")
  }

  test("can define map element as implicit") {

    def loader(): Try[String] = {
      Success("original data")
    }

    loader().map { implicit data =>
      process1().map(x => process2)
    }.map { finalData =>
      finalData.flatten shouldBe Some("original data-status2")
    }
  }

  test("can define map element with multiple args as implicit") {

    def loader(): Try[(String, Int)] = {
      Success("my order", 100)
    }

    loader().map { case (order: String, weight: Int) =>
      implicit val myOrderInfo: String = order
      process1().map(x => process2)
    }.map { finalData =>
      finalData.flatten shouldBe Some("my order-status2")
    }
  }
}
