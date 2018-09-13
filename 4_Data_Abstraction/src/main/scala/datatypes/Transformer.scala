package datatypes

import datatypes.DatatypesCats.getCustomerOrder

import scala.concurrent.Await
import scala.util.{Failure, Success}

final case class CustomerOrder(id: String, customer: String, items: List[String], amount: Double, status: String)

final case class OrderStatus(customer: String, message: String)

object Transformer {

  def main(args: Array[String]): Unit = {

    //    val ms = List(Map('A -> "ab"), Map('B -> "cd"), Map('A -> "we"))
    //    ms.foldMap(_.map { case (k, v) => (k, List(v)) })

    //    ms.map { case (k, v) => k }

    //    new (IList[CustomerOrder] ~> List[OrderStatus]) {
    //      override def apply[A](fa: IList[A]): List[A] = fa.toList
    //    }

    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.util.Try

    val fto: Future[Try[Option[Double]]] = Future.successful(Success(Option(100d)))
    val fo2 = Future.successful(Option(200d))

    val fo1 = fto.map {
      case Success(s) => s
      case Failure(_) => None
    }

    val y = for {
      f1 <- fo1
      f2 <- fo2
    } yield f1.flatMap(f => f2.map(_ + f))

    Thread.sleep(1000)
    println(y)

    //scalaz
  }

}
