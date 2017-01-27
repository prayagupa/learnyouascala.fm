package generics.dispatchhh

/**
  * Created by prayagupd
  * on 1/26/17.
  */

sealed trait BaseEvent

final case class E1(num: Int) extends BaseEvent
final case class E2(str: String) extends BaseEvent
final case class E3(d: Double) extends BaseEvent

//trait Reader[A <: BaseEvent, B <: BaseEvent, C <: Consumer2[A, B]] {
//  def read(stream: Seq[BaseEvent])
//}
//
//class EventReader[A <: BaseEvent, B<: BaseEvent, C <: Consumer2[A, B]](implicit consumer: Consumer2[A, B]) extends Reader[A, B, C]{
//  //reads A and B only
//  override def read(stream: Seq[BaseEvent]): Unit = {
//    stream.foreach(consumer.consume)
//  }
//}

trait Consumable[E] {
  def consume(event: E): Unit
}

trait Consumer {
  def consume[E: Consumable](event: E): Unit
}

abstract class Consumer2[A, B] extends Consumer {
  def consume[E: Consumable](event: E) = implicitly[Consumable[E]].consume(event)
  def consumeA(event: A): Unit
  def consumeB(event: B): Unit
}

abstract class Consume2Object[A, B, C <: Consumer2[A, B]] {
  implicit def AConsumer(implicit consumer: Consumer2[A, B]): Consumable[A] = new Consumable[A] {
    override def consume(event: A) = consumer.consumeA(event)
  }
  implicit def BConsumer(implicit consumer: Consumer2[A, B]): Consumable[B] = new Consumable[B] {
    override def consume(event: B) = consumer.consumeB(event)
  }
}

class SomeConcreteConsumer extends Consumer2[E1, E2] {
  override def consumeA(event: E1): Unit = println(s"Some consumed an E1 with value $event")
  override def consumeB(event: E2): Unit = println(s"Some consumed an E2 with value $event")
}
object SomeConcreteConsumer extends Consume2Object[E1, E2, SomeConcreteConsumer]

class AnotherConcreteConsumer extends Consumer2[E1, E3] {
  override def consumeA(event: E1): Unit = println(s"Another consumed an E1 with value $event")
  override def consumeB(event: E3): Unit = println(s"Another consumed an E3 with value $event")
}
object AnotherConcreteConsumer extends Consume2Object[E1, E3, AnotherConcreteConsumer]

object MyApp extends App {

  someExample()
  anotherExample()

  def someExample(): Unit = {
    import SomeConcreteConsumer._
    implicit val consumer = new SomeConcreteConsumer

    //new EventReader[E1, E2, Consumer2[E1, E2]]().read(Seq(E1(1), E2("joker")))
  }

  def anotherExample(): Unit = {
    import AnotherConcreteConsumer._
    implicit val consumer = new AnotherConcreteConsumer

    //new EventReader[E1, E3,  Consumer2[E1, E3]]().read(Seq(E1(1), E3(3.1415)))
  }
}
