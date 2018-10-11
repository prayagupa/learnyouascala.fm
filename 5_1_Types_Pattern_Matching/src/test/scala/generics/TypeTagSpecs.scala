package generics

import org.scalatest.{FunSuite, Matchers}

class TypeTagSpecs extends FunSuite with Matchers {

  test("maniefst") {

    object TypeExtractor {
      def extract[T](list: List[Any]): List[T] = list.flatMap {
        //TODO warning
        case element: T => Some(element)
        case _ => None
      }
    }

    val list = List(1, "order-data-1", List(), "order-data-2")
    val result = TypeExtractor.extract[String](list)

    result shouldBe List(1, "order-data-1", List(), "order-data-2")

  }

  test("filter values by type") {

    import scala.reflect.ClassTag
    object Extractor {
      def extract[T](list: List[Any])(implicit tag: ClassTag[T]): List[T] =
        list.flatMap {
          case element: T => Some(element)
          case _ => None
        }
    }
    val list: List[Any] = List(1, "order-data-1", List(), "order-data-2")
    val result = Extractor.extract[String](list)

    result shouldBe List("order-data-1", "order-data-2")
  }

  test("get runtime class from classTag from className") {

    import scala.reflect.ClassTag
    import scala.util.{Failure, Success, Try}

    def toDesiredType[T](clazz: Class[_])(implicit clazzTag: ClassTag[T]): Try[T] = {

      clazzTag match {
        case t: T => Success(t)
        case t => Failure(new Exception(s"$clazz is not of type ${clazzTag.runtimeClass}"))
      }
    }

    // with string
    val myString = toDesiredType[java.lang.String](Class.forName(classOf[java.lang.String].getName))

    myString shouldBe Success(classOf[java.lang.String])

    // example 2
    case class SomeProcessor(processorName: String)
    val processorClass: Class[_] = Class.forName(classOf[SomeProcessor].getName)

    val handler: Try[SomeProcessor] = toDesiredType[SomeProcessor](processorClass)

    handler shouldBe Success(classOf[SomeProcessor])
  }

  //https://stackoverflow.com/a/21286390/432903
  test("real patten") {
    import reflect.runtime.universe._

    def toHandler[T: TypeTag](classOfSomeType: Class[T]) = classOfSomeType match {
      case h if typeOf[T] <:< typeOf[String] => println("String")
      case h if typeOf[T] <:< typeOf[Int] => println("Int")
      case a => println(typeOf[T])
    }

    val h: Class[_] = Class.forName(classOf[String].getName)

    toHandler(h)
  }

  test("get typeTag") {
    import scala.reflect.runtime.universe._

    def whatListAmI[A: TypeTag](list: Class[A]): TypeTag[A] = {
      if (typeTag[A] == typeTag[java.lang.String]) // note that typeTag[String] does not match due to type alias being a different type
        println("its a String")
      else if (typeTag[A] == typeTag[Int])
        println("its a Int")

      typeTag[A]
    }

    whatListAmI(classOf[String]) shouldBe typeTag[String]
  }

  test("get typeTag from className") {
    import scala.reflect.runtime.universe._

    def getTypeTag[A: TypeTag](myClass: Class[A]): TypeTag[A] = {
      if (typeTag[A] == typeTag[java.lang.String])
        println("its a String")

      typeTag[A]
    }

    getTypeTag(classOf[java.lang.String]) shouldBe typeTag[java.lang.String]


  }

  //  test("real") {
  //    import scala.reflect.runtime.universe._
  //
  //    class MyHandler() {}
  //
  //    def getTypeTag[A: TypeTag](myClass: Class[A]): Try[A] = {
  ////      if (typeTag[A] == typeTag[MyHandler])
  ////        println("its a MyHandler")
  //
  //      typeTag[A]
  //    }
  //
  //    getTypeTag(classOf[MyHandler])
  //  }

  test("Class[T] to newInstance") {

    val clazz = Class.forName("scala.collection.mutable.StringBuilder")
    clazz.newInstance().asInstanceOf[StringBuilder].append("updating stuffs")

  }

  test("from typeTag[T] to Class[T] to newInstance") {
    import scala.reflect.runtime.universe._

    def newInstance[T: TypeTag] = {
      val clazz: Class[_] = typeTag[T].mirror.runtimeClass(typeOf[T])
      clazz.newInstance.asInstanceOf[T]
    }

    val s = newInstance[StringBuilder].append("a")

    s.toString() shouldBe "a"

  }
}
