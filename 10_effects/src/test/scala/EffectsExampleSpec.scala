import java.util.concurrent.Executors

import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.ExecutionContext

// https://typelevel.org/cats-effect/datatypes/io.html
class EffectsExampleSpec extends FunSuite with Matchers {


  test("future specs") {

    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext

    import scala.concurrent.ExecutionContext.Implicits.global

    def getPeople = Future {
      List("Steven", "Wilson", "Michael")
    }

    def getStatus(name: String) = Future {
      s"$name updated"
    }

    val updatedPeopleUsingSeq: Future[List[String]] = getPeople.flatMap { people =>
      Future.sequence {
        people.map(getStatus)
      }
    }

    Thread.sleep(1000)

    println(updatedPeopleUsingSeq)
  }

  test("deferring effects") {

    import cats.effect.IO

    val writeData: IO[Unit] = IO {
      println("read from database")
    }

    val program: IO[Unit] = for {
      data <- writeData
      data2 <- writeData
    } yield ()

    val execution = program.attempt

    execution.map {
      case Right(r) => println(r)
      case Left(l) => println(l)
    }

    program.unsafeRunSync()
  }

  test("pure effect") {

    import cats.effect.IO

    val application = IO.pure(100)
      .flatMap(price => IO {
        price * 0.80
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

  test("effect again") {

    import cats.effect.IO

    val nonBlockingExecContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

    val res = for {
      _ <- IO {
        println(Thread.currentThread().getName)
      }
      _ <- IO.shift(nonBlockingExecContext)
      _ <- IO {
        println(Thread.currentThread().getName)
      }
    } yield ()

    res.unsafeRunSync()
  }
}
