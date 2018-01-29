package generics

import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/24/17.
  */

trait BaseEvent{}
case class Event1() extends BaseEvent
case class Event2() extends BaseEvent

trait Consumer[E1 <: BaseEvent, E2 <: BaseEvent] {
  def consume1(event: E1)
  def consume2(event: E2)
  def setEventTypes(eventTypes: List[Class[_ <: BaseEvent]]) : Consumer[E1, E2]
}

abstract class TwoEventsConsumer[E1 <: BaseEvent, E2 <: BaseEvent] extends Consumer[E1, E2]{
  var eventTypes : List[Class[_ <: BaseEvent]] = _

  override def consume1(event: E1): Unit = {

  }

  override def consume2(event: E2): Unit = {

  }

  override def setEventTypes(eventTypes: List[Class[_ <: BaseEvent]]): Consumer[E1, E2] = {
    this.eventTypes = eventTypes
    this
  }
}

class MultiGenericsSpecs extends FunSuite {
  test("multi generics") {
    val x = new TwoEventsConsumer[Event1, Event2]{
      setEventTypes(List(classOf[Event1], classOf[Event2]))
    }

    assert(x.eventTypes == List(classOf[Event1], classOf[Event2]))
  }
}
