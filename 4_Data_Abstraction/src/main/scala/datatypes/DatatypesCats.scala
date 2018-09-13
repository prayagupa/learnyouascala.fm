package datatypes

import cats.data.OptionT
import cats.instances.future._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.Random

object DatatypesCats {

  final case class Order(id: String, amount: Int)

  def getCustomerOrder(id: String): Future[Option[Order]] =
    if (id.toInt < 10)
      Future.successful(Option(Order(id, Random.nextInt(100))))
    else Future.successful(None)

  def main(args: Array[String]): Unit = {

    val order1 = OptionT(getCustomerOrder(1.toString))
    val order2 = OptionT(getCustomerOrder(2.toString))
    val order3 = OptionT(getCustomerOrder(20.toString))

    val totalPriceOpt = for {
      o1 <- order1
      o2 <- order2
      o3 <- order3
    } yield o1.amount + o2.amount + o3.amount

    import scala.concurrent.duration._

    val totalPrice = Await.result(totalPriceOpt.value, 1 seconds)
    println(totalPrice)

    //
    import cats.implicits._
    val res = for {
      a <- getCustomerOrder(1.toString)
      b <- getCustomerOrder(2.toString)
      c <- getCustomerOrder(20.toString)
    } yield a.map(_.amount) |+| b.map(_.amount) |+| c.map(_.amount)

    val totalAmountt = Await.result(res, 1 seconds)
    println(totalAmountt)
  }

}
