package generics

import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/17/17.
  */

trait SomeFruit {
  def name() : String
}

case class Orange(name: String, eaten: Boolean = false) extends more.IsFruit

trait EdibleFruit[T <: more.IsFruit] {

  def eat(list: List[T]) : List[T]

}

class EdibleOrange extends EdibleFruit[more.Orange] {
  override def eat(list: List[more.Orange]): List[more.Orange] = list.map(x => more.Orange(x.name, eaten = true))
}

class MoreGenericsSpecs extends FunSuite {

  test("extends a trait") {
    val apples = new EdibleOrange
    val returned = apples.eat(List(more.Orange(name = "apple1"), more.Orange(name = "apple2")))
    assert(returned.map(_.name) == List("apple1", "apple2"))
    assert(returned.map(_.eaten) == List(true, true))
  }
}
