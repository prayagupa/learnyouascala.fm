import org.scalatest.FunSuite

/**
  * Created by prayagupd
  * on 1/18/17.
  */

trait Machine {
  def hertz : String
}

case class TV(hertz : String) extends Machine
case class Radio(hertz : String) extends Machine

trait Broadcastable[M <: Machine] {
  def machine(machineType: String, hertzz: String) : M
}

abstract class AbstractBroadcastable[A <: Machine] extends Broadcastable[A] {
  def machineType(implicit m: Manifest[A]): Class[_]

  override def machine(machineTypeee: String, hertzz: String): A = {
    val machine = new Machine {
      override def hertz: String = hertzz
    }

    //val classT : Class[_] = machineType

    println("=================================")
    println(machine.getClass)
    //println(machine.asInstanceOf[classT.type ])
    println("=================================")

    return machine.asInstanceOf.asInstanceOf[A]
  }
}

class TVBroadcastable extends AbstractBroadcastable[TV] {
  def machineType(implicit m: Manifest[TV]): Class[_] = m.runtimeClass
}

class ScalaReflectionSpecs extends FunSuite {
  test("returns generics class type") {
    println(s"${new TVBroadcastable().machineType.getName} == ${classOf[TV].getName}")
    assert(new TVBroadcastable().machineType.getName == classOf[TV].getName)

    println(s"${new TVBroadcastable().machine("TV", "12").getClass.getName} == ${classOf[TV].getName}")
    assert(new TVBroadcastable().machine("TV", "322").getClass.getName == classOf[TV].getName)
  }
}
