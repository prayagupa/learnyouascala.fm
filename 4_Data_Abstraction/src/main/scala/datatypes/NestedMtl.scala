package datatypes

object NestedMtl {

  import cats.data.Nested
  import cats.instances.option._
  import cats.syntax.functor._
  import cats.data.Validated
  import cats.data.Validated.Valid

  def main(args: Array[String]): Unit = {

    val validOrderID: Validated[String, Int] = Valid(111)

    //val nested: Nested[Option, Validated[String, ?], Int] = Nested(Option[Int](validOrderID))
    val nested = Nested(Some(validOrderID))

  }
}
