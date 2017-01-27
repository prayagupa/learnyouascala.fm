package generics.dispatch

/**
  * Created by prayagupd
  * on 1/26/17.
  */

object MyApp extends App {
  val e1 = Event1(1)
  val e2 = Event2("test")
  val e3 = Event3(3.14)

  someExample()
  anotherExample()

  def someExample(): Unit = {
    import MyEventsHandler._
    implicit val consumer = new MyEventsHandler

    consumer.consume(e1)
    consumer.consume(e2)
    // Doesn't compile: consumer.consume(e3)
  }
  def anotherExample(): Unit = {
    import YourEventsHandler._
    implicit val consumer = new YourEventsHandler

    //consumer.consume(e1)
    // Doesn't compile: consumer.consume(e2)
    //consumer.consume(e3)

    new EventReader().process()
  }
}
