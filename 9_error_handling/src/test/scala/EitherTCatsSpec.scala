import org.scalatest.{FunSuite, Matchers}

class EitherTCatsSpec extends FunSuite with Matchers {

  import scala.concurrent.Await
  import scala.util.Try
  import cats.implicits._

  def divide(a: Double, b: Double): Either[String, Double] =
    Either.cond(b != 0, a / b, "Cannot divide by zero")

  def parseDouble(s: String): Either[String, Double] =
    Try(s.toDouble).map(Right(_)).getOrElse(Left(s"$s is not a number"))

  test("Either I") {

    def divisionProgram(inputA: String, inputB: String): Either[String, Double] =
      for {
        a <- parseDouble(inputA)
        b <- parseDouble(inputB)
        result <- divide(a, b)
      } yield result

    divisionProgram("4", "2") shouldBe Right(2)

    divisionProgram("a", "b") shouldBe Left("a is not a number")

  }

  test("Either II") {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future
    import scala.concurrent.Await

    def parseDoubleAsync(s: String): Future[Either[String, Double]] =
      Future.successful(parseDouble(s))

    def divideAsync(a: Double, b: Double): Future[Either[String, Double]] =
      Future.successful(divide(a, b))

    def divisionProgramAsync(inputA: String, inputB: String): Future[Either[String, Double]] =
      parseDoubleAsync(inputA) flatMap { eitherA =>
        parseDoubleAsync(inputB) flatMap { eitherB =>
          (eitherA, eitherB) match {
            case (Right(a), Right(b)) => divideAsync(a, b)
            case (Left(err), _) => Future.successful(Left(err))
            case (_, Left(err)) => Future.successful(Left(err))
          }
        }
      }


    import scala.concurrent.duration._
    val result = Await.result(divisionProgramAsync("100", "2"), 1 seconds)
    result shouldBe Right(50)

    val result2 = Await.result(divisionProgramAsync("a", "2"), 1 seconds)
    result2 shouldBe Left("a is not a number")
  }

  test("EitherT") {
    import cats.data.EitherT
    import cats.implicits._
    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    import scala.concurrent.Await

    def parseDoubleAsync(s: String): Future[Either[String, Double]] =
      Future.successful(parseDouble(s))

    def divideAsync(a: Double, b: Double): Future[Either[String, Double]] =
      Future.successful(divide(a, b))

    def divisionProgramAsync(inputA: String, inputB: String): EitherT[Future, String, Double] =
      for {
        a <- EitherT(parseDoubleAsync(inputA))
        b <- EitherT(parseDoubleAsync(inputB))
        result <- EitherT(divideAsync(a, b))
      } yield result

    val result1: EitherT[Future, String, Double] = divisionProgramAsync("4", "2")
    Await.result(result1.value, 1 seconds) shouldBe Right(2)

    val result2: EitherT[Future, String, Double] = divisionProgramAsync("a", "b")
    Await.result(result2.value, 1 seconds) shouldBe Left("a is not a number")
  }

}
