#Scala 

## Table of Contents

  1. [General Questions](#general-questions)
  2. [Language Questions](https://github.com/prayagupd/learnyouascala.fm/blob/master/LanguageQN.md#language-questions)
  3. [FP Questions](https://github.com/prayagupd/learnyouascala.fm/blob/master/FP.md#functional-programming-questions)
  4. [Reactive Programming Questions](https://github.com/prayagupd/learnyouascala.fm/blob/master/Reactive.md#reactive-programming-questions)
  5. [Coding Questions](#coding-questions)
  6. [Fun Questions](#fun-questions)

#### General Questions:

* What did you learn yesterday/this week?
* Why and how did you start learning Scala?
* What excites or interests you about coding in Scala?
* What is a recent technical challenge you experienced and how did you solve it?
* Talk about your preferred development environment. (OS, Editor or IDE, Tools, etc.)
* What are your thoughts about the other JVM languages compared to Scala?
* Do you think that the Scala language and community is mature enough?

#### Coding Questions:

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
