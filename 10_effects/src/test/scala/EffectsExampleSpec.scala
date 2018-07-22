import org.scalatest.{FunSuite, Matchers}

// https://typelevel.org/cats-effect/datatypes/io.html
class EffectsExampleSpec extends FunSuite with Matchers {

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

    val application = IO.pure("customer id").flatMap(id => IO {
      println(s"""customer: $id""")
    })

    application.attempt
  }

}
