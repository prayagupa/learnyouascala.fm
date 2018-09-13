package datatypes

import datatypes.DatatypesCats.getCustomerOrder

import scalaz._
import Scalaz._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.Random
import scalaz.Maybe.Just

object DatatypesScalaz {

  final case class Order(id: String, amount: Int)

  def getCustomerOrder(id: String): Future[Option[Order]] =
    if (id.toInt < 10)
      Future.successful(Option(Order(id, Random.nextInt(100))))
    else Future.successful(None)

  def main(args: Array[String]): Unit = {

    //OptionT
    val order1 = OptionT(getCustomerOrder(1.toString))
    val order2 = OptionT(getCustomerOrder(2.toString))
    val order3 = OptionT(getCustomerOrder(20.toString))

    val totalPriceOpt = for {
      o1 <- order1
      o2 <- order2
      o3 <- order3
    } yield o1.amount + o2.amount + o3.amount

    import scala.concurrent.duration._

    val totalPrice = Await.result(totalPriceOpt.run, 1 seconds)

    println(totalPrice)

    //
    import scalaz._
    import Scalaz._
    import scalaz.Maybe.Empty

    val res = for {
      a <- getCustomerOrder(1.toString)
      b <- getCustomerOrder(2.toString)
      c <- getCustomerOrder(20.toString)
    } yield a.map(_.amount) |+| b.map(_.amount) |+| c.map(_.amount)

    import scala.concurrent.duration._
    val totalAmountt = Await.result(res, 1 seconds)
    println(totalAmountt)

    // Maybe
    final case class Item(name: String, qty: Int, price: Double)

    val item1: Maybe[Item] = Just(Item(name = "Shirts", qty = 1, price = 100d))
    val item2: Maybe[Item] = Just(Item(name = "Pants", qty = 2, price = 200d))
    val item3: Maybe[Item] = Empty[Item]()

    val inv = item1.map(_.qty) |+| item2.map(_.qty) |+| item3.map(_.qty)
    println(inv)

    //Validation
    final case class Authentication(username: String, password: String)


    //These
  }

}
