#Scala Job Interview Questions

This file contains a number of Scala interview questions that can be used when vetting potential candidates. It is by no means recommended to use every single question here on the same candidate (that would take hours). Choosing a few items from this list should help you vet the intended skills you require.

**Note:** Keep in mind that many of these questions are open-ended and could lead to interesting discussions that tell you more about the person's capabilities than a straight answer would.

## Table of Contents

  1. [General Questions](#general-questions)
  2. [Language Questions](https://github.com/prayagupd/Scala-Interview-Questions/blob/master/LanguageQN.md#language-questions)
  3. [FP Questions](https://github.com/prayagupd/Scala-Interview-Questions/blob/master/FP.md#functional-programming-questions)
  4. [Reactive Programming Questions](https://github.com/prayagupd/Scala-Interview-Questions/blob/master/Reactive.md#reactive-programming-questions)
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

## Getting Involved

  1. [Contributors](#contributors)
  1. [How to Contribute](https://github.com/jarlakxen/Scala-Interview-Questions/blob/master/CONTRIBUTING.md)
  1. [License](https://github.com/jarlakxen/Scala-Interview-Questions/blob/master/LICENSE.md)
