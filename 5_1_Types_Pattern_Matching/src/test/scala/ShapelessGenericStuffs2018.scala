import shapeless.syntax.SingletonOps

object GenericStuffs2018 {

  // https://harrylaou.com/slides/shapeless101.pdf

  def main(args: Array[String]): Unit = {

    import shapeless._

    val intents = Map(
      "phone repair" -> List(
        "how can i repair my phone" :: 1 :: HNil,
        "I need to repair my phone" :: 2 :: HNil
      )
    )

    final case class PhoneRepair(problem: String, id: Int)

    val g = Generic[PhoneRepair]

    val is = intents.flatMap {
      case ("phone repair", v) => v.map(g.from)
    }

    println(is)

    /////
    ////// Polymorphic functions
    /////

    /// natural transformation
    import poly._

    object uiView extends (List ~> Option) {
      override def apply[T](f: List[T]): Option[T] = f.headOption
    }

    val ui = uiView(List(PhoneRepair("here is how you can repair", 1)))
    println(ui)

    //// via type classes
    object polyLength extends Poly1 {
      implicit def caseInt = at[Int](x => 1)

      implicit def caseString = at[String](_.length)

      //      implicit def caseTuple[A, B]
      //      (implicit ap: Case.Aux[A, Int],
      //                bp: Case.Aux[B, String]) =
      //        at[(A, B)](ab => polyLength(ab._1) + polyLength(ab._2))

      implicit def caseTuple[A, B]
      (implicit st: Case.Aux[A, Int], su: Case.Aux[B, Int]) =
        at[(A, B)](t => polyLength(t._1) + polyLength(t._2))

    }

    assert(polyLength(89) == 1)
    assert(polyLength("updupdupd") == 9)
    assert(polyLength((1, "upd")) == 4)

    ////
    //// CartesianProduct/ CoProduct
    ////

    ////
    //// The Aux: A simple type with one type parameter
    ////

    trait IntentHandler[a] {
      type b

      def value: b
    }

    final case class PhoneRepairIntent(query: String)
    final case class PhoneRepairResponse(message: String, instructions: List[String])

    implicit def phoneRepairHandler = new IntentHandler[PhoneRepairIntent] {
      type b = PhoneRepairResponse

      def value: b = PhoneRepairResponse("Please follow the steps below to repair", List(
        "throw the phone in bin"
      ))
    }

    def uiProcessor[a](intent: a)(implicit handler: IntentHandler[a]): handler.b = {
      handler.value
    }

    val a1 = uiProcessor(PhoneRepairIntent("my phone does not wake up"))
    println("PhoneRepairResponse: " + a1)

    //// But what happens if we want to use the return type as a type
    //// parameter in a type constructor ???


    /////
    ///// Singleton types
    /////
    import shapeless._
    import syntax.singleton._
    val dataSingleton = 89.narrow

    /////
    ///// Witness type
    /////
    ///// To get a value from a singleton type.
    //// With Witness.

    // If we have a witness for a singleton type in scope
    val wt = Witness(89)
    println("witness: " + wt)
    val dataSingleont2: Int = wt.value
    println("witness value: " + dataSingleont2)
  }

  def x = {
    val a : Iterable[String] = List("1", "2", "3")
    a match {
      case head :: Nil => println("one element")
      case head :: tail :: Nil => println("two elements")
      case head :: tail :: Nil => println("two elements")
    }
  }
}
