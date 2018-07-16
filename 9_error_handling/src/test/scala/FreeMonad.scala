import cats.free.Free
import cats.~>
import cats._, alleycats.std.all._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
//
//sealed trait External[A]
//case class Orders(count: Int) extends AnyVal
//case class InvokeOrderService(count: Int) extends External[Orders]
//case class OrderRequest(itemCount: Int)
//
//object GetOrdersExample {
//
//  def purchaseOrders(input: OrderRequest): Free[External, Option[Orders]] = {
//    if (input.itemCount > 0) {
//      // creates a "Suspend" node
//      Free.liftF(InvokeOrderService(input.itemCount)).map(Some(_))
//    } else {
//      Free.pure(None)
//    }
//  }
//
//  def bonusOrders(purchased: Option[Orders]): Free[External, Option[Orders]] = {
//    if (purchased.exists(_.count > 10)) {
//      Free.liftF(InvokeOrderService(1)).map(Some(_))
//    } else {
//      Free.pure(None)
//    }
//  }
//
//  def formatResponse(purchased: Option[Orders], bonus: Option[Orders]): String =
//    s"Purchased tickets: $purchased, bonus: $bonus"
//
//  val input = OrderRequest(11)
//
//  val logic: Free[External, String] = for {
//    purchased <- purchaseOrders(input)
//    bonus <- bonusOrders(purchased)
//  } yield formatResponse(purchased, bonus)
//
//  val externalToServiceInvoker = new (External ~> Future) {
//    override def apply[A](e: External[A]): Future[A] = e match {
//      case InvokeOrderService(c) => serviceInvoker.run(s"/tkts?count=$c")
//    }
//  }
//
//  val result = logic.foldMap(externalToServiceInvoker)
//  result.foreach(println)
//
//  object serviceInvoker {
//    def run(path: String) = {
//      Future {
//        Orders(11)
//      }
//    }
//  }
//}


//sealed trait External[A]
//
//case class Tickets(count: Int) extends AnyVal
//
//case class InvokeTicketingService(count: Int) extends External[Tickets]
//
//case class UserTicketsRequest(ticketCount: Int)
//
//object GetTicketsExample {
//
//  def purchaseTickets(input: UserTicketsRequest): Free[External, Option[Tickets]] = {
//    if (input.ticketCount > 0) {
//      // creates a "Suspend" node
//      Free.liftF(InvokeTicketingService(input.ticketCount)).map(Some(_))
//    } else {
//      Free.pure(None)
//    }
//  }
//
//  def bonusTickets(purchased: Option[Tickets]): Free[External, Option[Tickets]] = {
//    if (purchased.exists(_.count > 10)) {
//      Free.liftF(InvokeTicketingService(1)).map(Some(_))
//    } else {
//      Free.pure(None)
//    }
//  }
//
//  def formatResponse(purchased: Option[Tickets], bonus: Option[Tickets]): String =
//    s"Purchased tickets: $purchased, bonus: $bonus"
//
//  val input = UserTicketsRequest(11)
//
//  val logic: Free[External, String] = for {
//    purchased <- purchaseTickets(input)
//    bonus <- bonusTickets(purchased)
//  } yield formatResponse(purchased, bonus)
//
//  val externalToServiceInvoker = new (External ~> Future) {
//    override def apply[A](e: External[A]): Future[A] = e match {
//      case InvokeTicketingService(c) => serviceInvoker.run(s"/tkts?count=$c")
//    }
//  }
//
//  val result = logic.foldMap(externalToServiceInvoker)
//  result.foreach(println)
//
//  object serviceInvoker {
//    def run(path: String) = {
//      Future {
//        Tickets(11)
//      }
//    }
//  }
//
//  def main(args: Array[String]): Unit = {
//    print("==============================")
//  }
//}