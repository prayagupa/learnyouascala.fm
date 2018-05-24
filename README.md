#Scala 

## Table of Contents

  1. [General Questions](#general-questions)
  2. [Language Questions](https://github.com/prayagupd/learnyouascala.fm/blob/master/LanguageQN.md#language-questions)
  3. [FP Questions](https://github.com/prayagupd/learnyouascala.fm/blob/master/FP.md#functional-programming-questions)
  4. [Reactive Programming Questions](https://github.com/prayagupd/learnyouascala.fm/blob/master/Reactive.md#reactive-programming-questions)
  5. [Coding Questions](#coding-questions)
  6. [Fun Questions](#fun-questions)

#### General Questions:

* What did you learn yesterday/this week?
* Why and how did you start learning Scala?
* What excites or interests you about coding in Scala?
* What is a recent technical challenge you experienced and how did you solve it?
* Talk about your preferred development environment. (OS, Editor or IDE, Tools, etc.)
* What are your thoughts about the other JVM languages compared to Scala?
* Do you think that the Scala language and community is mature enough?

#### Coding Questions:

* What is the difference (if any) between these 2 statements? 

```scala
  var x = immutable.Set[Int]()
  val y = mutable.Set[Int]()
```

#### Fun Questions:

* What's a cool project that you've recently worked on?
* What testing framework for Scala do you use?
* What do you know about property based testing frameworks, such as Scalacheck?
* Do you like ‘scalaz‘?

```scala
// Scala library for functional programming.

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.4"

scala> import scalaz._
import scalaz._

// functions and type class instances for Option and List
scala> import std.option._, std.list._
import std.option._
import std.list._

scala> final case class Item(name: String, price: Int)
defined class Item

scala> Apply[Option].apply2(some(Item("guiter", 100)), some(Item("amp", 200)))((item1, item2) => item1.price + item2.price)
res0: Option[Int] = Some(300)
```

```scala
scala> Traverse[List].traverse(List(Item("guitar", 1), Item("amp", 2)))(i => some(i))
res1: Option[List[Item]] = Some(List(Item(guitar,1), Item(amp,2)))

scala> Traverse[List].traverse(List.empty[Item])(i => some(i))
res3: Option[List[Item]] = Some(List())
```

get all items purchased

```scala
scala> import scalaz._, Scalaz._
import scalaz._
import Scalaz._

scala> final case class Order(orderId: String, items: List[Item])
defined class Order

scala> List(Order("1", List(Item("guiter", 1))), Order("2", List(Item("amp", 2)))).map(_.items).join
res13: List[Item] = List(Item(guiter,1), Item(amp,2))
```

```scala
scala> final case class Order(orderId: String, items: List[Item], status: String)
defined class Order

scala> val orders = List(Order("1", List(Item("guiter", 1)), "delivered"), Order("2", List(Item("amp", 2)), "delivered"), Order("1", List(Item("drum kit", 3)), "processing"))
orders: List[Order] = List(Order(1,List(Item(guiter,1)),delivered), Order(2,List(Item(amp,2)),delivered), Order(1,List(Item(drum kit,3)),processing))

scala> List(orders.filter(_.status == "processing").size > 0, orders.filter(_.status == "processing").size > 0).ifM(orders.filter(_.status == "processing"), orders)
res21: List[Order] = List(Order(1,List(Item(drum kit,3)),processing), Order(1,List(Item(drum kit,3)),processing))
```
