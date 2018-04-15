#### Functional Programming Questions:

* How can you make a `List[String]` from a `List[List[String]]`?

```scala
// http://alvinalexander.com/scala/how-to-flatten-list-lists-in-scala-with-flatten-method

val listOfSiteEvents = List(List(1,2), List(3,4))
val result = listOfSiteEvents.flatten

// .map/ .sort
listOfSiteEvents.flatten
                .map(event => event * 2)
                .sorted
```

What are typeclasses?
---------------------

The idea of `typeclass`es is that you provide evidence that a class satisfies an interface.
(OO Factory pattern)

- http://www.cakesolutions.net/teamblogs/demystifying-implicits-and-typeclasses-in-scala
- http://eed3si9n.com/learning-scalaz/day1.html
- http://learnyouahaskell.com/types-and-typeclasses

```scala
trait CanDoSomething[A] { //Typeclass (looks like interface??)
  def doSomething(x: A): String
}

case class Bird(sound: String)

object BirdCanDoSomething extends CanDoSomething[Bird] {
  def doSomething(x: Bird) = x.sound
}
```

Problem: 
If you want to take a thing that `CanDoSomething`, you need to both ask for 
* the class instance and 
* the proof from your caller.

```scala
def doSomething[A](thing: A, evidence: CanDoSomething[A]) = evidence.doSomething(thing)
```

     
Solution: Bird and CanDoSomething in scope

```scala
implicit object BirdCanDoSomething extends CanDoSomething[Bird] {
  def doSomething(x: Bird) = 
       x.sound
}

def doSomething[A](thing: A)(implicit evidence: CanDoSomething[A]) = 
      evidence.doSomething(thing)
      
doSomething(Bird("quack"))

//using syntactic sugar
def doSomething[A:CanDoSomething](thing: A) = 
     implicitly[CanDoSomething[A]].doSomething(thing)
```

[What are type classes in Scala useful for?](http://stackoverflow.com/a/5426131/432903)
<table>
<tr>
<td>interface</td>
<td>typeclass</td>
</tr>
<tr>
<td>
A crucial distinction between type-classes and interfaces is that: 
for class A to be a "member" of an interface it must declare so at the site of its own definition. 
</td>
<td>
By contrast, any type can be added to a type-class at any time, 
provided you can provide the required definitions, and so the members of a type class 
at any given time are dependent on the current scope. 
</td>
</tr>
</table>

Addable typeclass;

```scala
//yup, it's our friend the monoid, with a different name!
trait Addable[T] {
  def zero: T //identity monad
  def append(a: T, b: T): T
}

implicit object IntIsAddable extends Addable[Int] {
  def zero = 0
  def append(a: Int, b: Int) = 
      a + b
}

implicit object StringIsAddable extends Addable[String] {
  def zero = ""
  def append(a: String, b: String) = 
      a + b
}

def sum[T](xs: List[T])(implicit addable: Addable[T]) =
  xs.FoldLeft(addable.zero)(addable.append)

//or the same thing, using context bounds:

def sum[T : Addable](xs: List[T]) = {
  val addable = implicitly[Addable[T]]
  xs.FoldLeft(addable.zero)(addable.append)
}
```

[What is a `Functor[T]`? (covariant[+] contravariant[-], exponential, applicative, monad, co-monad)](https://gist.github.com/prayagupd/33b2886b2f7b06cb767ad60fdd38a2d2#functors)
-------------------------

- scalaz, http://eed3si9n.com/learning-scalaz/Functor.html

things(I'd say collection(eg. List, Future)) that can be mapped/composed over

- http://blog.tmorris.net/posts/functors-and-things-using-scala/index.html
- [Scala Functor and Monad differences](http://stackoverflow.com/a/8464561/432903)

Functor[T[_]] transforms a T[A] into a T[B] by applying the function funcX.

```scala

// covariant functor
// covariant functor —> defines the ops map or fMap.
trait Functor[T[_]]{
  def map[A, B](funcX : A => B)(ta:T[A]) : T[B]
}
// that's exactly what a functor[T] is.
```

Just what is a Functor, really?
Functor is a typeclass.

```
// haskell -> Box analogy
// http://learnyouahaskell.com/functors-applicative-functors-and-monoids

Functor[T]s are things that can be mapped over ( like List[T], Maybe[T], Tree[T], and such).

In Haskell, they're described by the typeclass Functor[T], which has only one typeclass method, namely fmap, which has a type of 
      fmap :: (a -> b) 
                 -> f a 
                    -> f b
      
It says: 
* give me a function that takes a and returns b 
* and box with an a (or several of them) inside it and 
* I'll give you a box with b (or several of them) inside it. 

It kind of applies the function to the element inside the box.
```

```scala

// Ex. http://www.casualmiracles.com/2012/01/08/a-small-functor-example/

object Functorise extends App {
 
  // This is a Variant Functor
  trait Functor[M[_]] {
 
    /* convert f into a function mapping M[A] to M[B]
     * 
     * eg. if M were List, and f was (Int => String)
     * fMap would yield (List[Int] => List[String])
     */
    def fMap[A, B](f: A => B): 
                          M[A] => M[B]
  }
 
  /* Here are a couple of examples for Option and List Functors
   * They are implicit so they can be used below in enrichWithFunctor
   */
 
  implicit 
  object OptionFunctor extends Functor[Option] {
    def fMap[A, B](f: A => B): Option[A] => Option[B] = option => option map f
  }
 
  implicit 
  object ListFunctor extends Functor[List] {
    def fMap[A, B](f: A => B): List[A]  => List[B] = list => list map f
  }
 
  /* enrichWithFunctor is an implicit to enrich any kind with an fMap method.
   * List, Option and any other Foo[X] can be enriched with the
   * new method.
   */
  implicit 
  def enrichWithFunctor[M[_], A](implicitTypeMDelegator: M[A]) = new {
 
    /* fMap requires an implicit functor, whose type is M, to which it
     * delegates to do the real work
     */
    def mapWith[B](func: A => B)(implicit functor_could_be_list: Functor[M]): M[B] =
         functor_could_be_list.fMap(func)(implicitTypeMDelegator) //calls OptionFunctor.fMap or ListFunctor.fMap
  }
 
  // usage
 
  println(List(2, 8) mapWith (_ + 1))              // List(3, 9)
 
  println(some(1) mapWith (_ + 1) mapWith (_ * 3)) // Some(6)
 
  println(none[Int] mapWith (_ + 1))               // None
 
  def some[A](a: A): Option[A] = Some(a)
  def none[A]: Option[A] = None
}

```

What is a `Applicative[T[_]]`?
------------------------------

Background
-------------

* Functor[T] allow us to apply fns to things in a context. 

* So far, 
when we were mapping fn over functors, we usually mapped fns that take only one parameter. 
But, 
what happens when we map a fn like `*`, which takes 2 parameters, over a functor?

But, what if the fns we want to apply are already in a context? 

```scala
And is pretty easy to end in that situation if you have fns that take > 1 parameter.

//solution
Now we need something like a Functor[] but that also takes functions already in the context and applies them to  
elements in the context. And that's what the applicative functor is. 

* Applicative defines the operation commonly known as apply or <*>.

// Here is the signature:

trait Applicative[T[_]] extends Functor[T]{
  def pure[A](a:A) : T[A]
  def <*>[A,B](tf:T[A => B])(ta:T[A]) : T[B]
}

eg. 
scala> List(1, 2, 3, 4) map { (_: Int) * (_:Int) }.curried
res1: List[Int => Int] = List(scala.Function2$$Lambda$1630/334183260@5c4125a0, scala.Function2$$Lambda$1630/334183260@16bac4af, scala.Function2$$Lambda$1630/334183260@2e8da4c1, scala.Function2$$Lambda$1630/334183260@44c8d6f)

scala> res1 map {_(9)}
res2: List[Int] = List(9, 18, 27, 36)

// List (actually the list type constructor, []) is applicative functor. 
// What a surprise!
List(1, 2, 3) <*> List((_: Int) * 0, (_: Int) + 100, (x: Int) => x * x)

val f = Apply[Option].lift2(
         (_: Int) :: (_: List[Int])
        )
f(3.some, List(4).some)
```

<h2>So far so good.</h2> 
What if now you have a function that puts things in the context? 


Now comes the monads: 
--------------------

```
// It's signature will be g:X => M[X] ... you can't use a functor because it expects X => Y 
// so we'll end with M[M[X]], you can't use the applicative functor because is expecting the fn already in the 
// context M[X => Y] .

// So we use a monad, that takes a fn X => M[X] and something already in the context M[A] and applies the fn 
// to what's inside the context, packing the result in only one context. 
```

```scala
// defines the operation commonly known as bind, flatMap or =<<.
// http://blog.tmorris.net/posts/functors-and-things-using-scala/index.html

// The signature is: 
trait Monad[M[_]] extends Applicative[M]{
  def >>= [A, B](ma : M[A])(f : A => M[B]) : M[B]
}
```

* What is a `monad`?
  * What are the `monad` axioms?
  * What Scala data types are, or behave like, monads?
  * What are the basic and optional requirement/s to conform a Monad?

```
//https://en.wikipedia.org/wiki/Monad_(functional_programming)
* In FP, a monad is a structure that represents computations defined as sequences of steps: a type with a monad structure defines what it means to chain operations, or nest functions of that type together. 
* This allows the programmer to build pipelines that process data in steps, in which each action is decorated with additional processing rules provided by the monad.
* As such, monads have been described as "programmable semicolons"; a semicolon is the operator used to chain together individual statements in many imperative programming languages, thus the expression implies that extra code will be executed between the statements in the pipeline.
```

```
eg, Maybe monad, // https://en.wikipedia.org/wiki/Option_type
data Maybe weightOfPackage = Just weightOfPackage | Nothing //in haskell

io monad, 
doesFileExist :: FilePath -> IO Bool
removeFile :: FilePath -> IO ()

// groovy
// https://mperry.github.io/2014/09/10/groovy-monad-combinators.html

// JS
// https://blog.jcoglan.com/2011/03/05/translation-from-haskell-to-javascript-of-selected-portions-of-the-best-introduction-to-monads-ive-ever-read/
// https://blog.jcoglan.com/2011/03/11/promises-are-the-monad-of-asynchronous-programming/
// http://api.jquery.com/category/deferred-object/
```

* Explain HOF - Higher Order Functions.
```scala

// https://gleichmann.wordpress.com/2010/11/28/high-higher-higher-order-functions/

// HOFs are fns that take other fns as parameters, or whose result is a fn.

val  filter = ( predicate :Int => Boolean, list :List[Int] )  =>  {
    for(  elem <- list;  if predicate( elem )  )  yield elem
}

val evenPredicate =  ( x :Int ) => x % 2 == 0
val oddPredicate  =  ( x :Int ) => x % 2 == 1

val candidates = List( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 )
 
val evenValues = filter( evenPredicate, candidates )
val oddValues = filter( oddPredicate, candidates )
```

* What is gained from using immutable objects?
* What is tail recursion?
  * How does it differentiate from common recursion?
  * What issues are there with tail recursive functions in the JVM?
  * How does the Scala compiler optimize a tail recursive function?
  * How do you ensure that the compiler optimizes the tail recursive function?
* What is function currying?
* What are implicit parameters?
```
// http://daily-scala.blogspot.com/2010/04/implicit-parameters.html
// Implicit parameters provide a way to allow parameters of a method to be "found". 
// This is similar to default parameters at a glance but in fact is a different mechanism for finding the 
// "default" value.

def pay(implicit userId:Int) = {
    println(userId)
    //do db stuffs
}

implicit val userId=89 //considered during implicit resolution

pay      // will print 89
pay(19) // explicit overrides implicit
```

* What are lenses?
```scala

// problem in nested objects
case class Person(firstName: String, lastName: String, address: Address)
case class Address(street: String, city: String, state: String, zipCode: Int)

val person = Person("Prayag", "Upd", Address("1000 N", 
                                           "FF", 
                                           "IA", 
                                           52556))

//update addresss -> zipcode, 52556
val updatedPerson = person.copy(address = person.address.copy(zipCode = person.address.zipCode + 1))

//Lens

val addressZipCodeLens = Lens(
    get = (_: Address).zipCode,
    set = (addr: Address, zipCode: Int) => addr.copy(zipCode = zipCode))

val personAddressLens = Lens(
    get = (_: Person).address, 
    set = (p: Person, addr: Address) => p.copy(address = addr))
    
// The big deal about lenses is that they are composable. 
// So they are a bit cumbersome at first, but they keep gaining ground the more you use them. 
// Also, they are great for testability, since you only need to test individual lenses, and 
// can take for granted their composition.

```

* What is and which are the uses of: Enumerators, Enumeratees and Iteratee
```scala
// http://mandubian.com/2012/08/27/understanding-play2-iteratees-for-normal-humans/
// http://jsuereth.com/scala/2012/02/29/iteratees.html
// http://stackoverflow.com/a/10184005/432903
object Application extends Controller {
  
  /** 
   * A String Enumerator producing a formatted Time message every 100 millis.
   * A callback enumerator is pure an can be applied on several Iteratee.
   */
  lazy val clock: Enumerator[String] = {
    
    import java.util._
    import java.text._
    
    val dateFormat = new SimpleDateFormat("HH mm ss")
    
    Enumerator.fromCallback { () =>
      Promise.timeout(Some(dateFormat.format(new Date)), 100 milliseconds)
    }
  }
  
  def index = Action {
    Ok(views.html.index())
  }
  
  def liveClock = Action {
    Ok.stream(clock &> Comet(callback = "parent.clockChanged"))
  }
  
}
```
