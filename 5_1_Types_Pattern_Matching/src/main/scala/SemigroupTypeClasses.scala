object SemigroupTypeClasses {

  // https://typelevel.org/cats/typeclasses/semigroup.html
  def semigroup: Unit = {

    import cats.Semigroup

    implicit val intAdditionSemigroup: Semigroup[Int] = new Semigroup[Int] {
      def combine(x: Int, y: Int): Int = x + y
    }

    val left = Semigroup[Int].combine(1, Semigroup[Int].combine(2, 3))
    val right = Semigroup[Int].combine(Semigroup[Int].combine(1, 2), 3)

    println(left + " == " + right)

  }

  // https://nequissimus.com/scalaz/algebra/Semigroup.html
  // https://typelevel.org/cats/typeclasses/monoid.html
  def monoid = {

    import cats.Monoid

    implicit val intAdditionMonoid: Monoid[Int] = new Monoid[Int] {
      def empty: Int = 0

      def combine(x: Int, y: Int): Int = x + y
    }

    val left = Monoid[Int].combine(1, Monoid[Int].empty)
    val right = Monoid[Int].combine(Monoid[Int].empty, 1)

    println(left + " == " + right)
  }

  def main(args: Array[String]): Unit = {

    semigroup
    monoid
  }

}
