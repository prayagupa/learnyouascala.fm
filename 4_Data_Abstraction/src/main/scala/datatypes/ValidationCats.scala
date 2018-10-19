package datatypes

object ValidationCats {

  import cats.data.Validated
  import cats.syntax.either._
  import cats.instances.list._
  import cats.syntax.apply._

  object Validation {

    case class User(name: String, age: Int)

    type InputRequestBody = Map[String, String]
    type ResponseEither[A] = Either[List[String], A]
    type ResponseValidated[A] = Validated[List[String], A]

    def getValue(field: String)(data: InputRequestBody): ResponseEither[String] =
      data.get(field).toRight(List(s"$field field is not specified"))

    def parseInt(field: String)(data: String): ResponseEither[Int] =
      Either.catchOnly[NumberFormatException](data.toInt).leftMap(_ => List(s"$field must be a integer"))

    /*_*/
    def nonBlank(field: String)(data: String): ResponseEither[String] =
      Right(data).ensure(List(s"$field cannot be blank"))(_.nonEmpty)

    def nonNegative(name: String)(data: Int): ResponseEither[Int] =
      Right(data).ensure(List(s"$name must be non-negative"))(_ >= 0)

    /*_*/
    def readName(data: InputRequestBody): ResponseEither[String] =
      getValue("name")(data).flatMap(nonBlank("field"))

    def readAge(data: InputRequestBody): ResponseEither[Int] = getValue("age")(data).
      flatMap(nonBlank("age")).
      flatMap(parseInt("age")).
      flatMap(nonNegative("age"))

    def readUser(data: InputRequestBody): ResponseValidated[User] = {

      val validated: (Validated[List[String], String], Validated[List[String], Int]) = (
        readName(data).toValidated,
        readAge(data).toValidated
      )

      validated.mapN(User.apply)
    }

  }

  def main(args: Array[String]): Unit = {

    val user = Validation.readUser(
      Map(
        "name" -> "prayagupd",
        "age" -> "28"
      )
    )

    println(user)
  }

}
