package recfun

object BalanceParens {

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
}
