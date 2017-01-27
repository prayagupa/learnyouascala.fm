package generics.dispatch

/**
  * Created by prayag
  * on 1/26/17.
  */

abstract class EventDispatcher[A, B, H <: TwoEventsHandler[A, B]] {
  implicit def AConsumer(implicit consumer: TwoEventsHandler[A, B]): Consumable[A] = new Consumable[A] {
    override def consume(event: A) = consumer.consumeA(event)
  }
  implicit def BConsumer(implicit consumer: TwoEventsHandler[A, B]): Consumable[B] = new Consumable[B] {
    override def consume(event: B) = consumer.consumeB(event)
  }
}

class EventReader(implicit consumer: Consumer) {

  import MyEventsHandler._
  val eventStream = List(Event1(1), Event2("test"), Event3(3.1415))

  def process(): Unit = {
    eventStream.foreach { event =>
      //consumer.consume(event)
    }
  }
}
