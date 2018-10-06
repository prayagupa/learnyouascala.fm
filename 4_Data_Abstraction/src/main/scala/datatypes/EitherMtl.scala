package datatypes

import cats.Id

//https://typelevel.org/cats/datatypes/eithert.html
object EitherMtl {

  def main(args: Array[String]): Unit = {
    import cats.data.EitherT
    import cats.implicits._

    val orderId: Either[String, Int] = Left("order not found")

    val dataT: EitherT[Id, String, Int] = EitherT.fromEither(orderId)

    println(dataT)

    val ordersList: Either[String, List[Int]] = Right(List(1, 2))

    val dataTr: EitherT[List, String, List[Int]] = EitherT.fromEither(ordersList)

    println(dataTr)

  }
}
