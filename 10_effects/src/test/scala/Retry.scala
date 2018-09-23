import scala.concurrent.Await

object Retry {

  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  def doSomething(times: Int): Future[Int] = {
    if (times < 1) {
      Future.failed(new RuntimeException("tried my best, handle better"))
    } else {
      Future {
        1 / 0
      } recoverWith { case e =>
        doSomething(times - 1)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    import scala.concurrent.duration._
    val d = Await.result(doSomething(3), 1 seconds)
    println("result: " + d)
  }
}
