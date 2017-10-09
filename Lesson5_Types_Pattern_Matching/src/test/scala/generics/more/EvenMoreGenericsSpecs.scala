package generics.more

import generics.more
import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/17/17.
  */

class MoreGenericsSpecs extends FunSuite {

  test("extends a trait") {
    val edibleFruits = new EditableFruits
    val x = edibleFruits.eatFruits(classOf[Banana].getSimpleName, Map("banana1" -> true))
    assert(x.head.name == "banana1")
    assert(x.head.eaten)
  }
}


//prod

trait IsFruit {
  def name() : String
  def eaten() : Boolean
}

case class Banana(name: String, eaten: Boolean = false) extends IsFruit
case class Orange(name: String, eaten: Boolean = false) extends IsFruit

trait EdibleFruit[T <: IsFruit] {
  def eatFruits(kindOfFruit: String, fruits: Map[String, Boolean]) : List[T]
}

abstract class AbstractEdibleFruit[T <: IsFruit] extends EdibleFruit[T] {

  def eat(fruit: T)

  override def eatFruits(kindOfFruit: String, fruits: Map[String, Boolean]): List[T] = {
    println(fruits.size)
    val realFruits = fruits.map(x => {
      kindOfFruit match {
        case "Banana" => Banana(name = x._1, eaten = x._2)
        case "Orange" => Orange(name = x._1, eaten = x._2)
      }
    }).toList
    realFruits.asInstanceOf[List[T]]
  }
}

class EditableFruits extends AbstractEdibleFruit[Banana] {

  override def eat(fruit: Banana): Unit = {
    println(s"Eating ${fruit.name}")
  }
}