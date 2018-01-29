package generics.dispatch

import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/26/17.
  */

class ConsumerSpecs extends FunSuite {

  test("consumes") {
    val e1 = Event1(1)
    val e2 = Event2("test")
    val e3 = Event3(3.14)

    someExample()
    anotherExample()

    def someExample(): Unit = {
      import MyTwoEventsHandler._
      implicit val consumer = new MyTwoEventsHandler

      consumer.consume(e1)
      consumer.consume(e2)
      // Doesn't compile:
      // consumer.consume(e3)
    }

    def anotherExample(): Unit = {
      implicit val consumer = new YourTwoEventsHandler

      //consumer.consume(e1)

      // Doesn't compile:
      // consumer.consume(e2)
      //consumer.consume(e3)

      new EventReader().process()
    }
  }
}
