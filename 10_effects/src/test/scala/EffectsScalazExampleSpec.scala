import java.io.IOException

import org.scalatest.{FunSuite, Matchers}

import scalaz.zio.App

object T extends App {

  import scalaz.zio.IO
  import scalaz.zio.console._

  def myAppLogic: IO[IOException, Unit] =
    for {
      _ <- putStrLn("Username: ")
      n <- getStrLn
      _ <- putStrLn("Hello, " + n + ", good to meet you!")
    } yield ()

  override def run(args: List[String]): IO[Nothing, ExitStatus] = {
    myAppLogic.attempt.map(_.fold(_ => 1, _ => 0)).map(ExitStatus.ExitNow(_))
  }
}
