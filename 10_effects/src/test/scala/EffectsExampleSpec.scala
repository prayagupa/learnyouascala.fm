import java.util.concurrent.Executors

import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.ExecutionContext

// https://typelevel.org/cats-effect/datatypes/io.html
class EffectsExampleSpec extends FunSuite with Matchers {

  test("non blocking futures") {

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

  test("cats Resource") {

    import cats.effect.{IO, Resource}
    import cats.implicits._

    def mkResource(res: String): Resource[IO, String] = {
      val acquire = IO(println(s"Acquiring $res")) *> IO.pure(res)

      def release(res: String) = IO(println(s"Releasing $res"))

      Resource.make(acquire)(release)
    }

    val out: Resource[IO, String] = mkResource("outer")
    val in: Resource[IO, String] = mkResource("inner")

    val resources = out.flatMap { a =>
      in.flatMap { b =>
        Resource.pure((a, b))
      }
    }

    val r = for {
      outer <- out
      inner <- in
    } yield (outer, inner)

    val res: String = resources.use { case (r1, r2) =>
      IO(println(s"Using $r1 and $r2")) *> IO.pure(r1 + "-" + r2)
    }.unsafeRunSync

    println(res)
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
