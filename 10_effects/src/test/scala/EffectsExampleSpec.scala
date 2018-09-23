import java.util.concurrent.Executors

import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.ExecutionContext

// https://typelevel.org/cats-effect/datatypes/io.html
class EffectsExampleSpec extends FunSuite with Matchers {

  test("future specs") {

    import scala.concurrent.Future

    implicit val nonBlockingExCtx: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    def getCustomerIds = Future {
      List("ID01", "ID02", "ID03")
    }

    def getStatus(id: String) = Future {
      s"IN PROGRESS"
    }

    val deliverStatusUsingSeq: Future[List[String]] = getCustomerIds.flatMap { ids =>
      Future.sequence {
        ids.map(id => getStatus(id))
      }
    }

    Thread.sleep(1000)

    println(deliverStatusUsingSeq)
  }

  test("deferring effects") {

    import cats.effect.IO

    val readData: IO[Unit] = IO {
      println("read from database")
    }

    val program: IO[Unit] = for {
      data <- readData
      data2 <- readData
    } yield ()

    val execution = program.attempt.map {
      case Right(r) => println(r)
      case Left(l) => println(l)
    }

    program.unsafeRunSync()
  }

  test("pure effect") {

    import cats.effect.IO

    final case class Item(name: String, price: Int)

    val application = IO.pure(Item("shirts", price = 100))
      .flatMap(item => IO {
        item.price * 0.80
      })
      .flatMap(totalPrice => IO {
        0 / 0
      })

    val result = application.attempt flatMap {
      case Right(r) => IO {
        println(s"$r")
      }
      case Left(l) => IO {
        println(s"error: $l")
      }
    }

    result.unsafeRunSync()
  }

  test("shift effect") {

    import cats.effect.IO

    val nonBlockingExecContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(
      Runtime.getRuntime.availableProcessors()
    ))

    val res = for {
      _ <- IO {
        println("thread:" + Thread.currentThread().getName + "-" + Thread.currentThread().getState)
      }
      _ <- IO.shift(nonBlockingExecContext)
      _ <- IO {
        println("thread:" + Thread.currentThread().getName + "-" + Thread.currentThread().getState)
      }
    } yield ()

    res.unsafeRunSync()
  }
}
