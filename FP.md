#### Functional Programming Questions:

* How can you make a `List[String]` from a `List[List[String]]`?
```
/http://alvinalexander.com/scala/how-to-flatten-list-lists-in-scala-with-flatten-method
val listOfEvents = List(List(1,2), List(3,4))
val result = listOfEvents.flatten

```

* What is a `functor`?
```scala
// http://stackoverflow.com/a/8464561/432903
trait Functor[T[_]]{
  def fMap[A,B](f:A=>B)(ta:T[A]):T[B]
}

// that's exactly what a functor is. It transforms a T[A] into a T[B] by applying the fn f.
```

* What is a `applicative`?
```scala
// Functor[T] allow us to apply fns to things in a context. 
// But what if the fns we want to apply are already in a context? 
// (And is pretty easy to end in that situation if you have fns that take more than 
// one parameter).

// Now we need something like a Functor but that also takes functions already in the context and applies them to  // elements in the context. And that's what the applicative functor is. Here is the signature:

trait Applicative[T[_]] extends Functor[T]{
  def pure[A](a:A):T[A]
  def <*>[A,B](tf:T[A=>B])(ta:T[A]):T[B]
}

//
// So far so good. 
// Now comes the monads: 
// what if now you have a function that puts things in the context? 
// It's signature will be g:X=>M[X] ... you can't use a functor because it expects X=>Y 
// so we'll end with M[M[X]], you can't use the applicative functor because is expecting the fn already in the 
// context M[X=>Y] .

// So we use a monad, that takes a fn X=>M[X] and something already in the context M[A] and applies the fn 
// to what's inside the context, packing the result in only one context. 
// The signature is:

trait Monad[M[_]] extends Applicative[M]{
  def >>=[A,B](ma:M[A])(f:A=>M[B]):M[B]
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

eg, Maybe monad, //https://en.wikipedia.org/wiki/Option_type
data Maybe x = Just x | Nothing //in haskell

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

* Explain Higher Order Functions.
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

* What are typeclasses?
```scala
// http://stackoverflow.com/a/5426131/432903

/**
A crucial distinction between type-classes and interfaces is that 
for class A to be a "member" of an interface it must declare so at the site of its own definition. 

By contrast, any type can be added to a type-class at any time, 
provided you can provide the required definitions, and so the members of a type class 
at any given time are dependent on the current scope. 
*/

//yup, it's our friend the monoid, with a different name!
trait Addable[T] {
  def zero: T
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
