import org.scalatest.{FunSuite, Matchers}

class TypeSpecs extends FunSuite with Matchers {

  test("richSeq") {

    implicit class RichSeq[T] (val seq: Seq[T]) {
      def fooExtension = "do something"
    }

    Seq(1, 2, 3).fooExtension shouldBe "do something"
    Seq("hi", "how are you").fooExtension shouldBe "do something"

  }

  test("rich customer") {

    implicit class Customer(val customerId: String) {
      def doSomething: String = customerId.toUpperCase
    }

    "apple".doSomething shouldBe "APPLE"
  }

  test("rich customer with Option") {

    implicit class Customer(val customerId: String) {
      def doSomething: String = customerId.toUpperCase
    }

    Option("customer_1").getOrElse("").doSomething shouldBe "CUSTOMER_1"
    Option.empty[String].getOrElse("").doSomething shouldBe ""
  }


  test("implicit customerId I") {

    case class Customer(customerId: CustomerId)

    implicit class CustomerId(val strId: String) {
      def apply: String = strId.toUpperCase
    }

    implicit class customerIdtoString(val customerId: CustomerId) {
      def apply: String = customerId.apply
    }

    implicit val customerId : CustomerId = Customer("customer_1").customerId

    //customerId shouldBe "CUSTOMER_1"

    customerId.apply shouldBe "CUSTOMER_1"

  }

  test("implicit customerId II") {

    type CustomerId = String

    case class Customer(customerId: CustomerId)

    implicit class ToCustomerId(val strId: String) {
      def apply: String = s"customer_${strId}"
    }

    implicit class customerIdtoString(val customerId: CustomerId) {
      def apply: String = customerId
    }

    val customerId : CustomerId = Customer("1").customerId

    customerId shouldBe "customer_1"

  }

  test("implicit customerId III with Option ") {

    type CustomerId = String

    case class Customer(customerId: CustomerId)

    implicit class ToCustomerId(val strId: String) {
      def apply: Customer = Customer("customer_" + strId)
    }

    implicit class customerIdtoString(val customerId: CustomerId) {
      def apply: String = customerId
    }

    Customer("1").customerId shouldBe "customer_1"

  }
}
