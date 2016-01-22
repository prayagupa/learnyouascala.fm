#### Language Questions:

* [What is the difference between a `var`, a `val` and `def`?]([http://stackoverflow.com/a/4440614/432903)

```scala
//http://stackoverflow.com/a/4440614/432903
//There are three ways of defining things in Scala:

def = { defines a method }
val = defines a fixed val (which cannot be modified)
var = defines a variable (which can be modified)
```

* What is the difference between a `trait` and an `abstract class`?
```
--------------------------------------------------------------------------------------------------------------------------
abstract class ConveyableItem [T]                               | trait Conveyable[T]
--------------------------------------------------------------------------------------------------------------------------
* can have type params(bounded) -> AbstractList[T]              | * can have only type params -> Generics[T]/ first-order types
           constructor params ->fns that construct other types  | //https://docs.oracle.com/javase/tutorial/java/generics/bounded.html
//http://www.hydrocodedesign.com/2014/04/02/higher-kinded-types/| 
// see bounds in scala for type params                          |
// Int, String, Boolean -> proper types                         |
                                                                |   ( There was some discussion  
* abstract class ConveyableItem(val itemId: Int,                |     that in future even traits can have 
                                val name: String,               |     constructor params. HOW can trait have constructor?)
                                val weight: T)                  |   
                                                                |
* are fully interoperable with Java <7.                         | * fully interoperable only if they do
  ( You can call them                                           |   not contain any implementation code
  from Java code without any wrappers.)                         |   
---------------------------------------------------------------------------------------------------------------------------

```

* What is a `sealed trait X`?
```
//http://stackoverflow.com/a/11203867/432903
A sealed trait can be extended only in the same file as its declaration.
```

* What is the difference between an `object` and a `class`?
* What is a `case class`?
```scala
// Case classes can be seen as plain and immutable data-holding objects 
// that should exclusively depend on their constructor args.

// eg.

sealed trait Entity
case class Visitor(var name: String, var geoLocation: String) extends Entity
 
val me = Visitor("Prayag", "IA, USA")
me.name = "NG" // call to a mutator

* useful for pattern matching, a more powerful relative of the 
  much-despised switch/case mechanism
```

* What is the difference between a Java `Future` and a Scala `Future` ? (TeachS, 2015)

```scala
//[What are the differences between a Scala Future and a Java Future](http://stackoverflow.com/a/31368177/432903)
-----------------------------------------------------------------------------------------------------------------
       java.util.concurrent.Future                        |    scala.concurrent.Future
-----------------------------------------------------------------------------------------------------------------
The main inconvenience of java.util.concurrent.Future     | With scala.concurrent.Future you get instead a real 
is the fact that you can't get the value                  | non-blocking computation as you can attach callbacks
without blocking.                                         | for completion (.success/.failure) or simply .map() over
In fact the only way to retrieve the val the get()        | it and chain multiple Futures together in a Monadic  
method                                                    | fashion.
-----------------------------------------------------------------------------------------------------------------
// http://www.javacodegeeks.com/2014/11/from-java-7-futures-to-akka-actors-with-scala.html
 							  |
public static class ItemLoader                            |  val clients = 1 until 10 toSeq   
     implements Callable<List> {                          |
   private final int clientId;                            |  // start the futures 
                                                          |  val itemFutures: Seq[Future[Seq[Item]]]=
  public ItemLoader(int clientId) {    	                  |   clients map { client => 
     this.clientId = clientId;	                          |     Future { 
  }                                                       |        new ItemService getItems client }
                                                          |    }
  @Override                                               |
  public List call() throws Exception {                   |
     ItemService service = new ItemService();             |
     return service.getItems(clientId);                   |
  }                                                       |
 } 						          |
                                                          |
//							  |
List<Integer> clients = ...;                              |       // convert list of futures to future of results 
int p = 4; //parallelism                                  |       val resultFuture: Future[Seq[Seq[Item]]] = 
ListeningExecutorService threadPool =                     |	      Future sequence itemFutures
MoreExecutors.listeningDecorator(Executors.newWorkStealingPool(p));
                                                          |       
// Submit all the futures                                 |       // flatten the result val itemsFuture: 
List<ListenableFuture<List>> itemFutures = new ArrayList<>();     Future[Seq[Item]] = 
for (Integer client : clients) {                          |            resultFuture map (_.flatten)
    ListenableFuture<List> future =                       | 
        threadPool.submit(new ItemLoader(client));        |       // blocking until all futures are finished, 
    itemFutures.add(future);                              |       // but wait at most 10 seconds   
}                                                         |       val items = Await.result(
                                                          |                     itemsFuture, 10 seconds)
//                                                        |
// convert list of futures to future of results           |
// Futures == com.google.common.util.concurrent.Futures   |
ListenableFuture<List<List<Item>>> resultFuture =         |
        Futures.allAsList(itemFutures);                   |
                                                          |
// blocking until finished - we only wait for a single    |
// Future to complete                                     |
List<List<Item>> itemResults = resultFuture.get();        |
-----------------------------------------------------------------------------------------------------------------
```

* What is the difference between `unapply` and `apply`, when would you use them?

```scala

// For regular parameters apply constructs and unapply de-structures:

object Event {
  def apply(a: A):Event = ...                // makes an Event from an A
  def unapply(event: Event): Option[A] = ... // retrieve the A from the Event
}
val e = Event(a)
e match { case Event(a) => a } 
```

* What is a companion `object`?
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

//companion usage
val service : EventService = new EventService()
service.creteEvent()
```

* What is the difference between the following terms and types in Scala: `Nil`, `Null`, `None`, `Nothing`? 
(TeachS, 2015, difference between Null and None)
```scala
// hierarchy -> http://www.scala-lang.org/old/node/71%3Fsize=preview.html
// http://blog.sanaulla.info/2009/07/12/nothingness/

--------------------------------------------------------------------------------------------------------
Null {}      | Its a Trait.
null         | Its an instance of Null- ( Similar to Java null)
--------------------------------------------------------------------------------------------------------
Nil          | Represents an emptry List of anything of zero length. 
             | Its not that it refers to nothing but it refers to List which has no contents.
--------------------------------------------------------------------------------------------------------
Nothing      | is a Trait. 
             | Its a subtype of everything. But not superclass of anything. 
             | There are no instances of Nothing.
--------------------------------------------------------------------------------------------------------
Some/None    | Used to represent a sensible return value. Just to avoid null pointer exception. 
             | Option[T] has exactly 2 subclasses- Some and None. 
             | None signifies no result from the method.
--------------------------------------------------------------------------------------------------------
Unit         | Type of method that doesn’t return a value of anys sort.
--------------------------------------------------------------------------------------------------------
// http://stackoverflow.com/a/16174738/432903
```

* What is `Unit`?
* What is the difference between a `call-by-value` and `call-by-name` parameter? 
	* How does Scala's `Stream` trait levarages `call-by-name`?
```scala

def getEventCount() = {
  println("calling getEventCount")
  1 // return value
}

-----------------------------------------------------------------------------------------
call-by-value version,                        | call-by-name version
-----------------------------------------------------------------------------------------
                                              |
def callByExecutedValue(f: Int) = {           | def callByFunctionName(f: => Int) = {
  println("f1=" + f)                          |   println("f1=" + f)
  println("f2=" + f)                          |   println("f2=" + f)
}                                             | }
                                              |
//usage                                       | 
scala> callByExecutedValue(getEventCount())   | scala> callByFunctionName(getEventCount())
calling getEventCount                         | calling getEventCount
f1=1                                          | f1=1
f2=1                                          | calling getEventCount
                                              | f2=1
                                              |
------------------------------------------------------------------------------------------
the side-effect of the passed-in function     | the side-effect happened twice.
call (getEventCount()) only happened once.    |
------------------------------------------------------------------------------------------

//[Call by name vs call by value in Scala, clarification needed](http://stackoverflow.com/a/17901633/432903)

```

* Define uses for the `Option` monad and good practices it provides. (TeachS, 2015)

```scala
// CT, Maths
In category theory, a branch of maths, a monad is an endofunctor (a functor mapping a category to itself), together with two natural transformations. 
Monads are used in the theory of pairs of adjoint functors, and they generalize closure operators on partially ordered sets to arbitrary categories.

// [What exactly makes Option a monad in Scala?](http://stackoverflow.com/a/25361305/432903)
// Scala
Monad is a concept, an abstract interface if you will, that simply defines a way of composing data.

Option[] supports composition via .flatMap, and that's pretty much everything that is needed to wear the "monad badge".

From a theoretical point of view, Option[] should also:
* support a unit operation (return, in Haskell terms) to create a monad out of a bare value, which in case of Option is the Some constructor
* respect the monadic laws
but this is not strictly enforced by Scala.

* Monads in scala are a much looser concept that in Haskell, and the approach is more practical. 
* The only thing monads are relevant for, from a language perspective, is the ability of being used in a for-comprehension.

.flatMap is a basic requirement, and you can optionally provide .map, .withFilter and .foreach.

However, there's no such thing as strict conformance to a Monad typeclass, like in Haskell.
```

```scala
// Here's an example: let's define our own monad.

class EventMonad[A](value: A) {
  def map[B](f: A => B) = new EventMonad(f(value))
  def flatMap[B](f: A => EventMonad[B]) = f(value)
  override def toString = value.toString
}

// Implementation
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
// [Where does Scala look for implicits?](http://stackoverflow.com/a/5598107/432903)
// Implicits in Scala refers to either a value that can be passed "automatically", so to speak, or a conversion 
// from one type to another that is made automatically.
```

```scala
Implicit Conversion - "anyString".map(_.toInt)

Implicit Parameters - def anyMethod[T](t: T)(implicit integral: Integral[T]) { 
                                println(integral)
                      }
View Bounds         - def getIndex[T, CC](seq: CC, value: T)(implicit conv: CC => Seq[T]) = seq.indexOf(value)
                      getIndex("anyString", 'a')
                      
Context Bounds      - 
def sum[T](list: List[T])(implicit integral: Integral[T]): T = {
    import integral._   // get the implicits in question into scope
    list.foldLeft(integral.zero)(_ + _)
}
```

Streams:
--------------

[Akka Stream and HTTP Experimental(http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-M2/AkkaStreamAndHTTPScala.pdf)

* What consideration you need to have when you use Scala's `Streams`? 
```
[Use-cases for Streams in Scala](stackoverflow.com/questions/2096876/use-cases-for-streams-in-scala)

immutable.Stream is to Iterator as immutable.List is to mutable.List. 

Favouring immutability prevents a class of bugs, occasionally at the cost of performance.
```

```scala
val streamOfInts = Stream.from(1)

streamOfInts.filter(_ % 5 == 0)
            .take(10)
            .toList

//output
  List(5, 10, 15, 20, 25, 30, 35, 40, 45, 50)

```

* What technique does the Scala's `Streams` use internally?

Generics
-----------

http://debasishg.blogspot.com/2009/01/higher-order-abstractions-in-scala-with.html
