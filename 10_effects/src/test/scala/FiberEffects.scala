import cats.effect.{ContextShift, Timer}
import org.scalatest.FunSpec

import scala.concurrent.ExecutionContext

class FiberEffects extends FunSpec {

  it("start cancel") {

    import cats.effect.IO
    import cats.implicits._
    import scala.concurrent.ExecutionContext

    // Needed for IO.start to do a logical thread fork
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    lazy val launchMissiles = IO.raiseError(new Exception("boom!"))
    lazy val runToBunker = IO(println("To the bunker!!!"))

    val whatever = for {
      missileFiber <- launchMissiles.start
      _ <- runToBunker.handleErrorWith { error =>
        (missileFiber.cancel: IO[Unit]) *> IO.raiseError(error)
      }
      aftermath <- missileFiber.join
    } yield {
      aftermath
    }

    whatever.unsafeRunSync()
  }

  it("cancel") {
    import scala.concurrent.duration._
    import scala.concurrent.ExecutionContext
    import cats.effect.IO
    import cats.implicits._

    // Needed for `sleep`
    implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

    // Delayed println
    val updateOrders: IO[Unit] = IO.sleep(10.seconds) *> IO(println("update orders!"))

    val cancelUpdate: IO[Unit] =
      updateOrders.unsafeRunCancelable(r => println(s"updating canceled: $r"))

    // ... if a race condition happens, we can cancel it,
    // thus canceling the scheduling of `IO.sleep`
    cancelUpdate.unsafeRunSync()
  }

  // https://typelevel.org/cats-effect/datatypes/io.html#example-retrying-with-exponential-backoff
  it("retry") {

    import cats.effect._
    import cats.syntax.all._
    import scala.concurrent.duration._

    def retryWithBackoff[A](ioOperation: Int => IO[A], initialDelay: FiniteDuration, maxRetries: Int)
                           (implicit timer: Timer[IO]): IO[A] = {

      ioOperation(maxRetries).handleErrorWith { error =>
        if (maxRetries > 0)
          IO(println(s"retrying $maxRetries")) *> IO.sleep(initialDelay) *> retryWithBackoff(ioOperation, initialDelay * 2, maxRetries - 1)
        else
          IO.raiseError(error)
      }
    }

    def businessLogic: Int => IO[String] = (id: Int) => {
      IO(println(s"received request $id")) *> IO.raiseError(new Exception("API error"))
    }

    implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)

    retryWithBackoff(businessLogic, 1 seconds, 2).unsafeRunSync()

  }
}
