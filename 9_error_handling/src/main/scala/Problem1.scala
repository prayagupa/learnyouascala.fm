import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

object Problem1 {

  def calculatePrice: String => Future[Try[Double]] = (priceString: String) => Future {
    Try {
      priceString.toDouble
    }
  }

  def calculateDiscount: (Double, Double) => Future[Try[Double]] = (price: Double, discount: Double) => Future {
    Try {
      price * discount
    }
  }

  def main(args: Array[String]): Unit = {

    // failure
    import scala.concurrent.duration._

//    lazy val badReading: Future[Double] = for {
//      priceTry <- calculatePrice("error")
//      discount <- Future.fromTry(priceTry)
//    } yield { discount }

    val failureReading: Future[Double] = calculatePrice("fail").flatMap {
      case Success(s) => println("sfds");Future.successful(s)
      case Failure(fail) => Future.failed(fail)
    }

    // good one
    lazy val goodReading: Future[Double] = for {
      priceTry <- calculatePrice("100")
      discount <- Future.fromTry(priceTry)
    } yield { discount }

    val g: Future[Double] = calculatePrice("100").flatMap {
      case Success(s) => Future.successful(s)
      case Failure(fail) => Future.failed(fail)
    }

    Thread.sleep(1000)

    println(failureReading)
    println(g)
  }

}
