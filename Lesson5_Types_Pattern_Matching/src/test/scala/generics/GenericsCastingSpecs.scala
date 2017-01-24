package generics

import org.scalatest.FunSuite

import scala.reflect.ClassTag

/**
  * Created by prayagupd
  * on 1/18/17.
  */

trait Vehicle {
  def engine() : String
}

case class Plane(engine: String) extends Vehicle

trait Flyable[F <: Vehicle] {
  def flies(engine: String) : F
}

class AbstractFlyable[F <: Vehicle] extends Flyable[F] {
  override def flies(vehicleEngine: String): F = {

    //val x= implicitly[ClassTag[F]].runtimeClass
    //println("====" + x)

    new Vehicle {
      override def engine(): String = vehicleEngine
    }.asInstanceOf[F]
  }
}

class PlaneFlyable extends AbstractFlyable[Plane] {

}

class GenericsCastingSpecs extends FunSuite {
  test("casts to generic object") {
    val planeFlyable = new PlaneFlyable
    val vehicle = planeFlyable.flies("as400")

    assert(vehicle.isInstanceOf[Plane])
  }
}
