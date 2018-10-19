package datatypes

import cats.Id

//https://typelevel.org/cats/datatypes/eithert.html
object EitherMtl {

  def main(args: Array[String]): Unit = {
    import cats.data.EitherT
    import cats.implicits._

    final case class Error(errorMessage: String)
    final case class Order(id: Int, price: Double)
    val orderId: Either[Error, Order] = Left(Error("order not found"))

    val dataT: EitherT[Id, Error, Order] = EitherT.fromEither(orderId)

    println(dataT)
    // EitherT(Left(Error(order not found)))

    val ordersList: Either[Error, List[Order]] = Right(List(Order(1, 10), Order(2, 20)))

    val dataTr: EitherT[List, Error, List[Order]] = EitherT.fromEither(ordersList)

    println(dataTr)
    // EitherT(List(Right(List(Order(1,10.0), Order(2,20.0)))))

  }
}
