
[Class Hierarchies](http://docs.scala-lang.org/tutorials/tour/unified-types)
---------------------

![hierarchy](http://joelabrahamsson.com/PageFiles/148/1311_1645.jpg)

How Classes Are Organized
-------------------------

abstract, trait

Polymorphism
------------

- Parametric Polymorphism
- Subtype Polymmorphism
- Ad-hoc Polymorphism

http://eed3si9n.com/learning-scalaz/polymorphism.html

https://twitter.github.io/scala_school/type-basics.html

[Abstract Types](http://docs.scala-lang.org/tutorials/tour/abstract-types.html)

http://x-wei.github.io/progfun1_lec3_data_abstraction.html


Object-Oriented Sets
--------------------

For this assignment,
`TweetSet` defines an `abstract class TweetSet` with two concrete subclasses,
`Empty` which represents an empty set, and `NonEmpty(elem: Tweet, left: TweetSet, right: TweetSet)`, 
which represents a non-empty set as a binary tree rooted at elem.

_The tweets are indexed by their text bodies: the bodies of all tweets on the left are 
lexicographically smaller than elem and all bodies of elements on the right 
are lexicographically greater._

_Note also that these classes are immutable: the set-theoretic operations 
do not modify this but should return a new set._

_Before tackling this assignment, we suggest you first study the already 
implemented methods `contains` and `incl` for inspiration._

```
  def contains(x: Tweet): Boolean =
    if (x.text < rootElem.text) left.contains(x)
    else if (rootElem.text < x.text) right.contains(x)
    else true

def include(tweetToAdd: Tweet): TweetSet = {
    if (tweetToAdd.text < elem.text) new OrderedSet(elem, left.include(tweetToAdd), right)
    else if (elem.text < tweetToAdd.text) new OrderedSet(elem, left, right.include(tweetToAdd))
    else this
  }
```

Filtering
---------

Implement filtering on tweet sets. Complete the stubs for the methods filter and filterAcc.

_`filter` takes as argument a function, the predicate, which takes a tweet and returns a boolean. 
`filter` then returns the subset of all the tweets in the original set for which the predicate is true._ 
For example, the following call:

```
.filter(tweet => tweet.retweets > 10)
```

applied to a set tweets of two tweets, say, where the first tweet was not retweeted and 
the second tweet was retweeted 20 times should return a set containing only the second tweet.

Taking Unions
---------------

Implement `union` on tweet sets. Complete the stub for the method union. 
_The method union takes another set that, and computes a new set which is the union of this and that, 
i.e. a set that contains exactly the elements that are either in this or in that, or in both._

```
def union(that: TweetSet): TweetSet
```

Sorting Tweets by Their Influence
-----------------------------------
The more often a tweet is “re-tweeted” 
(that is, repeated by a different user with or without additions), the more influential it is.

The goal of this part of the exercise is to add a method descendingByRetweet to 
TweetSet which should produce a linear sequence of tweets (as an instance of class TweetList), 
ordered by their number of retweets:

```
def descendingByRetweet: TweetList
```
