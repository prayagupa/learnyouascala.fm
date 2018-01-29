import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/23/17.
  */

class PolySpecs extends FunSuite {
  test("polymorphism") {
    val originalEvent : BaseEvent = SomethingHappened(0, classOf[SomethingHappened].getSimpleName, payload = "{}",
      newField = "apple")
    val event2 = originalEvent.copyy(100).asInstanceOf[SomethingHappened]

    assert(event2.eventOffset == 100)
    assert(event2.eventType == "SomethingHappened")
    assert(event2.newField == "apple")
  }
}

trait BaseEvent {

  def eventOffset: Long

  def eventType: String

  def copyy(eO: Long) : BaseEvent
}

case class SomethingHappened(eventOffset: Long, eventType: String, payload: String, newField: String) extends BaseEvent {

  override def copyy(eO: Long): BaseEvent = {
    this.copy(eventOffset = eO)
  }
}
