package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if ((c == 0 || r == 0) || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c - 0, r - 1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {

    def countOpenClosedParenthesis(chars: List[Char], balancedPars: Int): Int = {
      if (chars.isEmpty || balancedPars < 0) balancedPars
      else if (chars.head == '(') countOpenClosedParenthesis(chars.tail, balancedPars + 1)
      else if (chars.head == ')') countOpenClosedParenthesis(chars.tail, balancedPars - 1)
      else countOpenClosedParenthesis(chars.tail, balancedPars)
    }

    countOpenClosedParenthesis(chars, 0) == 0
  }

  def balanceStupidWay(chars: List[Char]): Boolean = {

    def findClosePar(chars: List[Char]): Boolean = {
      if (chars.isEmpty) return false
      if (chars.head == ')') true
      else findClosePar(chars.tail)
    }

    if (chars.head == '(')
      if (!findClosePar(chars.tail)) return false
      else balance(chars.tail)
    true
  }

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
