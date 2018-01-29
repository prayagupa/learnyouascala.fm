package generics

import org.scalatest.{FunSuite, Matchers}

/**
  * Created by prayagupd
  * on 1/17/17.
  */

class GenericsSpecs extends FunSuite with Matchers {

  test("extends a trait") {
    val apples = new EdibleApple
    val returned = apples.eat(List(Apple(name = "apple1"), Apple(name = "apple2")))
    assert(returned == List("Eating apple1.", "Eating apple2."))
  }

  test("can change trait") {

    import shapeless._
    import record._
    import syntax.singleton._

    trait Fruit { def id : Int}
    case class Banana(id: Int, name: String) extends Fruit

    implicit val lgenA = new LabelledGeneric[Fruit] {
      type Repr = Record.`'id -> Int`.T
      def to(fruit: Fruit) : Repr = ('id ->> fruit.id) :: HNil
      def from(repr: Repr): Fruit = new Fruit { val id = repr('id) }
    }

    val idLens = lens[Fruit] >> 'id
    val idSetLens = lens[Fruit] >> 'id

    val banana = Banana(1, "yellow")

    idLens.get(banana) shouldBe 1

    val newFruit = idLens.set(banana)(100)
    newFruit.id shouldBe 100
  }
}

//PROD

trait Fruit {
  val name : String
}

case class Apple(name: String) extends Fruit

trait Edible[T <: Fruit] {

  def eat(list: List[T]) : List[String]

}

class EdibleApple extends Edible[Apple] {
  override def eat(list: List[Apple]): List[String] = list.map(x => s"Eating ${x.name}.")
}
