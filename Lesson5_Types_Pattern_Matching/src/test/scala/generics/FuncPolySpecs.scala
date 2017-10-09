package generics

import java.time.LocalDateTime

import org.scalatest.{FunSuite, Matchers}
import shapeless.{Poly1, Poly2}

class FuncPolySpecs extends FunSuite with Matchers {

  test("dispatches based on type") {

    object Events {

      case class ItemOrdered(item: String, when: LocalDateTime)

      case class ReleaseItem(item: String, priority: Boolean)

      case class ShipItem(item: String, toLocation: String)

      case class ItemDelivered(item: String, deliveryDate: String)

    }

    import Events._

    object dispatch extends Poly1 {

      implicit def itemOrdered = at[ItemOrdered](o => ReleaseItem(o.item, true))

      implicit def itemShipped = at[ShipItem](o => ItemDelivered(o.item, "some-date"))

    }

    dispatch(ItemOrdered("shirts", LocalDateTime.now())) shouldBe ReleaseItem("shirts", true)
    dispatch(ShipItem("shirts", "to-location")) shouldBe ItemDelivered("shirts", "some-date")
  }

  trait Event

  object Event {

    case class ItemOrdered(item: String, when: LocalDateTime) extends Event

    case class ReleaseItem(item: String, priority: Boolean) extends Event

    case class ShipItem(item: String, toLocation: String) extends Event

    case class ItemDelivered(item: String, deliveryDate: String) extends Event

    def apply(eventType: String, item: String): Event = eventType match {
      case "ItemOrdered" => ItemOrdered(item, LocalDateTime.of(2017, 10, 28, 0, 0, 0))
      case "ShipItem" => ShipItem(item, "whereever")
    }
  }

  import Event._

  object dispatch extends Poly1 {

    implicit def itemOrdered = at[ItemOrdered] { o =>
      //
      //do bunch of stuffs
      //
      ReleaseItem(o.item, priority = true)
    }

    implicit def itemShipped = at[ShipItem] { o =>
      //
      //do bunch of stuffs
      //
      ItemDelivered(o.item, "some-date")
    }

    implicit def generics = at[Event] {
      case i: ItemOrdered => dispatch(i)
      case i: ShipItem => dispatch(i)
    }
  }

  test("dispatches based on sub-class") {

    dispatch(Event("ItemOrdered", "pants")) shouldBe ReleaseItem("pants", priority = true)

    dispatch(Event("ShipItem", "shoes")) shouldBe ItemDelivered("shoes", "some-date")

  }

  test("dispatches using pattern match") {

    object eventDispatcher {

      def eventDispatch(event: Event): Event = {
        event match {
          case io: ItemOrdered => ReleaseItem(io.item, priority = true)
          case si: ShipItem => ItemDelivered(si.item, "some-date")
        }
      }
    }

    import eventDispatcher._

    eventDispatch(Event("ItemOrdered", "pants")) shouldBe ReleaseItem("pants", priority = true)
  }

}
