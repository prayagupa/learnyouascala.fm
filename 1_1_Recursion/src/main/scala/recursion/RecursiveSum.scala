package recursion

object RecursiveSum {

  def sum(xs: List[Int]): Int = {
    if(xs.isEmpty) 0
    else xs.head + sum(xs.tail)
  }

  def max(xs: List[Int]): Int = {

    def maxRecursion(value: Int, list: List[Int]): Int = {
      if (list.isEmpty) value
      else value max maxRecursion(list.head, list.tail)
    }

    maxRecursion(xs.head, xs.tail)
  }
}
