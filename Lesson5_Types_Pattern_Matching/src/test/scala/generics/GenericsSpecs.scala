package generics

import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/17/17.
  */

trait Fruit {
  def name() : String
}

case class Apple(name: String) extends Fruit

trait Edible[T <: Fruit] {

  def eat(list: List[T]) : List[String]

}

class EdibleApple extends Edible[Apple] {
  override def eat(list: List[Apple]): List[String] = list.map(x => s"Eating ${x.name}.")
}

class GenericsSpecs extends FunSuite {

  test("extends a trait") {
    val apples = new EdibleApple
    val returned = apples.eat(List(Apple(name = "apple1"), Apple(name = "apple2")))
    assert(returned == List("Eating apple1.", "Eating apple2."))
  }
}
