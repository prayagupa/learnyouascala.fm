#Scala Job Interview Questions

This file contains a number of Scala interview questions that can be used when vetting potential candidates. It is by no means recommended to use every single question here on the same candidate (that would take hours). Choosing a few items from this list should help you vet the intended skills you require.

**Note:** Keep in mind that many of these questions are open-ended and could lead to interesting discussions that tell you more about the person's capabilities than a straight answer would.

## Table of Contents

  1. [General Questions](#general-questions)
  1. [Language Questions](#language-questions)
  1. [Functional Programming Questions](#functional-programming-questions)
  1. [Reactive Programming Questions](#reactive-programming-questions)
  1. [Coding Questions](#coding-questions)
  1. [Fun Questions](#fun-questions)

## Getting Involved

  1. [Contributors](#contributors)
  1. [How to Contribute](https://github.com/jarlakxen/Scala-Interview-Questions/blob/master/CONTRIBUTING.md)
  1. [License](https://github.com/jarlakxen/Scala-Interview-Questions/blob/master/LICENSE.md)

#### General Questions:

* What did you learn yesterday/this week?
* Why and how did you start learning Scala?
* What excites or interests you about coding in Scala?
* What is a recent technical challenge you experienced and how did you solve it?
* Talk about your preferred development environment. (OS, Editor or IDE, Tools, etc.)
* What are your thoughts about the other JVM languages compared to Scala?
* Do you think that the Scala language and community is mature enough?

#### Language Questions:

* [What is the difference between a `var`, a `val` and `def`?]([http://stackoverflow.com/a/4440614/432903)

```
There are three ways of defining things in Scala:

def # defines a method
val # defines a fixed val (which cannot be modified)
var # defines a variable (which can be modified)
```

* What is the difference between a `trait` and an `abstract class`?
```
-----------------------------------------------------------------------------------------------------------------------------------------------
abstract class                                                  | trait 
-----------------------------------------------------------------------------------------------------------------------------------------------
* can have constructor parameters as well as type parameters.   | * can have only type parameters. There was some
                                                                |   discussion that in future even
                                                                |   traits can have constructor parameters
                                                                |
* are fully interoperable with Java.                            | * Traits are fully interoperable only if they do
  You can call them                                             |   not contain any implementation code
  from Java code without any wrappers.                          |   
-----------------------------------------------------------------------------------------------------------------------------------------------

```

* What is the difference between an `object` and a `class`?
* What is a `case class`?

```
Case classes can be seen as plain and immutable data-holding objects that should exclusively depend on their constructor arguments.
```

* What is the difference between a Java future and a Scala future?
* What is the difference between `unapply` and `apply`, when would you use them?

```scala

// For regular parameters apply constructs and unapply de-structures:

object Event {
  def apply(a: A):Event = ... // makes an Event from an A
  def unapply(event: Event): Option[A] = ... // retrieve the A from the Event
}
val e = Event(a)
e match { case Event(a) => a } 
```

* What is a companion object?
```scala
// http://tutorials.jenkov.com/scala/singleton-and-companion-objects.html
// Scala classes cannot have static variables or methods. 
// Instead a Scala class can have what is called a singleton object,
// or sometime a companion object.

// A singleton object is declared using the object keyword. Here is an example:


class EventService {
    def addEventToQueue() = {
        println("add event to queue")
    }
}

object EventService {
    def creteEvent() {
        println("Event created!");
    }
}

//usage
val service : EventService = new EventService()
service.creteEvent()
```

* What is the difference between the following terms and types in Scala: `Nil`, `Null`, `None`, `Nothing`?
```scala
//http://blog.sanaulla.info/2009/07/12/nothingness/
Null– Its a Trait.
null–  Its an instance of Null- Similar to Java null.

Nil– Represents an emptry List of anything of zero length. Its not that it refers to nothing but it refers to List which has no contents.

Nothing is a Trait. Its a subtype of everything. But not superclass of anything. There are no instances of Nothing.

None– Used to represent a sensible return value. Just to avoid null pointer exception. Option has exactly 2 subclasses- Some and None. None signifies no result from the method.

Unit– Type of method that doesn’t return a value of anys sort.
```

* What is `Unit`?
* What is the difference between a `call-by-value` and `call-by-name` parameter? 
	* How does Scala's `Stream` trait levarages `call-by-name`?
```scala

-----------------------------------------------------------------------------------------
call-by-value version,                        | call-by-name version
-----------------------------------------------------------------------------------------
the side-effect of the passed-in function     | the side-effect happened twice.
call (getEventCount()) only happened once.    |
------------------------------------------------------------------------------------------

def getEventCount() = {
  println("calling getEventCount")
  1 // return value
}

def callByValue(f: Int) = {
  println("f1=" + f)
  println("f2=" + f)
}

def callByName(f: => Int) = {
  println("f1=" + f)
  println("f2=" + f)
}

//usage
scala> callByValue(getEventCount())
calling getEventCount
f1=1
f2=1

scala> callByName(getEventCount())
calling getEventCount
f1=1
calling getEventCount
f2=1

// http://stackoverflow.com/a/17901633/432903
```

* Define uses for the `Option` monad and good practices it provides.
```
// http://stackoverflow.com/a/25361305/432903
Monad is a concept, an abstract interface if you will, that simply defines a way of composing data.

Option supports composition via flatMap, and that's pretty much everything that is needed to wear the "monad badge".

From a theoretical point of view, Option should also:
* support a unit operation (return, in Haskell terms) to create a monad out of a bare value, which in case of Option is the Some constructor
* respect the monadic laws
but this is not strictly enforced by Scala.

* Monads in scala are a much looser concept that in Haskell, and the approach is more practical. 
* The only thing monads are relevant for, from a language perspective, is the ability of being used in a for-comprehension.

flatMap is a basic requirement, and you can optionally provide map, withFilter and foreach.

However, there's no such thing as strict conformance to a Monad typeclass, like in Haskell.

Here's an example: let's define our own monad.

class EventMonad[A](value: A) {
  def map[B](f: A => B) = new EventMonad(f(value))
  def flatMap[B](f: A => EventMonad[B]) = f(value)
  override def toString = value.toString
}

Implementation
As you see, we're only implementing map and flatMap (well, and toString as a commodity). Congratulations, we have a monad! Let's try it out:

scala> for {
  e1 <- new EventMonad(2)
  e2 <- new EventMonad(3)
} yield e1 + e2
// res1: EventMonad[Int] = 5

* Nice! We are not doing any filtering, so we don't need to implement withFilter. 
* Also since we're yielding a value, we don't need foreach either. 
* Basically you implement whatever you wish to support, without strict requirements. If you try to filter in a for-comprehension and you haven't implemented withFilter, you'll simply get a compile-time error.

* In Scala everything that has a flatmap is considered a "Monad" and it can be used in for-comprehensions (the equivalent of a do-block in haskell) 

```

* How does `yield` work?
```scala
// http://stackoverflow.com/a/1059501/432903

// Scala's "for comprehensions" is syntactic sugar for composition of multiple operations with map, flatMap and 
// filter. Or foreach. Scala actually translates a for-expression into calls to those methods, so any class 
// providing them, or a subset of them, can be used with for comprehensions.

for(x <- c1; y <- c2; z <- c3) yield {...}    | c1.flatMap(x => c2.flatMap(y => c3.map(z => {...})))
for(x <- c; if cond) yield {...}              | c.filter(x => cond).map(x => {...})
                                              | c.withFilter(x => cond).map(x => {...}) //scala 2.8
for(x <- c; y = ...) yield {...}              | c.map(x => (x, ...)).map((x,y) => {...})
```

* What operations is a `for comprehension` syntactic sugar for?

* Explain the implicit parameter precedence.
```
// http://stackoverflow.com/a/5598107/432903
// Implicits in Scala refers to either a value that can be passed "automatically", so to speak, or a conversion 
// from one type to another that is made automatically.

Implicit Conversion - "abc".map(_.toInt)
Implicit Parameters - def foo[T](t: T)(implicit integral: Integral[T]) {println(integral)}
View Bounds         - def getIndex[T, CC](seq: CC, value: T)(implicit conv: CC => Seq[T]) = seq.indexOf(value)
                      getIndex("abc", 'a')
Context Bounds      - 
def sum[T](list: List[T])(implicit integral: Integral[T]): T = {
    import integral._   // get the implicits in question into scope
    list.foldLeft(integral.zero)(_ + _)
}
```

* Streams:
	* What consideration you need to have when you use Scala's `Streams`? 
	* What technique does the Scala's `Streams` use internally?

#### Functional Programming Questions:

* What is a `functor`?
* What is a `applicative`?
* What is a `monad`?
  * What are the `monad` axioms?
  * What Scala data types are, or behave like, monads?
  * What are the basic and optional requirement/s to conform a Monad?
* Explain higher order functions.
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
* What are typeclasses?
* What are lenses?
* What is and which are the uses of: Enumerators, Enumeratees and Iteratee

#### Reactive Programming Questions:

* Explain the actor model.
* What are benefits of non-blocking (asynchronous I/O) over blocking (synchronous I/O).
* Do you think that Scala has the same async spirit as Node.js?
* Explain the difference between `concurrency` and `parallelism`, and name some constructs you can use in Scala to leverage both.
```

// http://stackoverflow.com/a/29796033/432903

-------------------------------------------------------------------------------------------------------------
Concurrency                                                   | Parallelism 
-------------------------------------------------------------------------------------------------------------
is when two tasks can start, run, and complete                | is when tasks literally run at the same time
in overlapping time periods.                                  | 
It doesn't necessarily mean they'll ever both                 | eg. on a multicore processor.
be running at the same instant.                               |
Eg. multitasking on a single-core machine.                    |
                                                              |
//
A condition that exists when                                  | condition that arises when at least two threads
at least two threads are making progress.                     | are executing simultaneously.
-------------------------------------------------------------------------------------------------------------
```
* What is the global ExecutionContext?
  * What does the global ExecutionContext underlay?
* What is the global ExecutionContext?
* Akka:
	* Which are the 3 main components in a Stream?

#### Coding Questions:

* How can you make a `List[String]` from a `List[List[String]]`?
* What is the difference (if any) between these 2 statements? 
```scala
  var x = immutable.Set[Int]()
  val y = mutable.Set[Int]()
```

#### Fun Questions:

* What's a cool project that you've recently worked on?
* What testing framework for Scala do you use?
* What do you know about property based testing frameworks, such as Scalacheck?
* Do you like ‘scalaz‘?
```scala
// Scala library for functional programming.

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.4"

import scalaz._
import std.option._, std.list._ // functions and type class instances for Option and List

Apply[Option].apply2(some(1), some(2))((a, b) => a + b)   // res0: Option[Int] = Some(3)
Traverse[List].traverse(List(1, 2, 3))(i => some(i))      // res1: Option[List[Int]] = Some(List(1, 2, 3))
List(List(1)).join                                        // res0: List[Int] = List(1)
List(true, false).ifM(List(0, 1), List(2, 3))             // res1: List[Int] = List(0, 1, 2, 3)

```

