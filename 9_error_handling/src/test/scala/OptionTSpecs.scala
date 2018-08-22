import org.scalatest.FunSuite

import cats.data.OptionT
import cats.implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

// https://typelevel.org/cats/datatypes/optiont.html
class OptionTSpecs extends FunSuite {

  def pickup(orderId: String): Future[Option[String]] = {
    Future.successful(Option(orderId))
  }

  def packup(orderId: String): Future[String] = {
    Future.successful(orderId)
  }

  test("option monad transformer") {

    val x: OptionT[Future, String] = OptionT(pickup("1"))

    val order: OptionT[Future, String] = for {
      pick <- OptionT(pickup("1"))
      pack <- OptionT.liftF(packup(pick))

    } yield pack

    Thread.sleep(1000)

    println(order.value)
  }

}
