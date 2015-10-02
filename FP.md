#### Functional Programming Questions:

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
* What are typeclasses?
* What are lenses?
* What is and which are the uses of: Enumerators, Enumeratees and Iteratee
