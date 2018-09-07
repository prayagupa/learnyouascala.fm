package recursion

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RecursiveSumSuite extends FunSuite {

  test("one plus one is two")(assert(1 + 1 == 2))

  test("one plus one is three?") {
    assert(1 + 1 != 3) // This assertion fails! Go ahead and fix it.
  }

  test("details why one plus one is not three") {
    assert(1 + 1 === 2) // Fix me, please!
  }

  test("intNotZero throws an exception if its argument is 0") {
    intercept[IllegalArgumentException] {
      intNotZero(0)
    }
  }

  def intNotZero(x: Int): Int = {
    if (x == 0) throw new IllegalArgumentException("zero is not allowed")
    else x
  }

  import RecursiveSum._

  test("sum of a few numbers") {
    assert(sum(List(1, 2, 0)) === 3)
  }

  test("max of a few numbers") {
    assert(max(List(3, 7, 2)) === 7)
  }


}
