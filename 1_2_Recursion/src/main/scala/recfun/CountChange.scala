package recfun

object CountChange {

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    println(s"for ($money, $coins) : ")
    if (money == 0) {
      println("1")
      1
    }
    else if (money < 0 || coins.isEmpty) {
      println("0")
      0
    }
    else if (money <= 0 && coins.nonEmpty) {
      println("0")
      0
    }
    else {
      println(": [" + money + ", " + coins.tail + "]" + "+ [" + (money - coins.head) + "," + coins + "]")
      countChange(money, coins.tail) + countChange(money - coins.head, coins)
    }
  }

}
