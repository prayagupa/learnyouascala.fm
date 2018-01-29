package generics.dispatch


/**
  * Created by prayagupd
  * on 1/26/17.
  */


sealed trait BaseEvent

final case class Event1(num: Int) extends BaseEvent
final case class Event2(str: String) extends BaseEvent
final case class Event3(d: Double) extends BaseEvent

trait Consumable[E] {
  def consume(event: E): Unit
}

trait Consumer {
  def consume[E: Consumable](event: E): Unit
}

abstract class TwoEventsHandler[A, B] extends Consumer {
  def consume[E: Consumable](event: E) = implicitly[Consumable[E]].consume(event)
  def consumeA(event: A): Unit
  def consumeB(event: B): Unit
}
