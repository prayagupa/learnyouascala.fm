[Higher Order Functions](https://www.coursera.org/learn/progfun1/home/week/2)
----


basic funs on Set
-----------------

```
// Set is represented by its characteristic function, i.e. contains predicate.
scala> type Set = (Int => Boolean)
defined type alias Set

scala> def contains(set: Set, elementToCheck: Int): Boolean = set(elementToCheck)
contains: (set: Set, elementToCheck: Int)Boolean

scala> def singletonSet(elem: Int): Set = { givenParamPassedToFn => givenParamPassedToFn == elem }
singletonSet: (elem: Int)Set
```

eg. 

```
scala> val mySet = singletonSet(89)
mySet: Set = $$Lambda$1552/139657330@1c4e9c50

scala> contains(mySet, 89)
res62: Boolean = true

scala> contains(mySet, 98)
res63: Boolean = false


```

Let’s start by implementing basic functions on sets.

1. Define a function `singletonSet` which creates a singleton set from one integer value: 
_the set represents the set of the one given element. Now that we have a way to create singleton sets, 
we want to define a function that allow us to build bigger sets from smaller ones._

```
def singletonSet(elem: Int): Set
```

2. Define the functions `union`, `intersect`, and `diff`, 
which takes two sets, and return, respectively, their union, intersection and differences. 
_diff(s, t) returns a set which contains all the elements of the set `s` 
that are not in the set `t`._

```
def union(s: Set, t: Set): Set
def intersect(s: Set, t: Set): Set
def diff(s: Set, t: Set): Set
```

3. Define the function `filter` which selects only the elements of a set that are 
accepted by a given predicate `p`. 
_The filtered elements are returned as a new set._

```
def filter(set: Set, p: (Int => Boolean)) : Set
```

Queries and Transformations on Sets
-----------------------------------

1. Implement `forall` using linear recursion. 
_For this, use a helper function nested `inforall`._

2. Using forall, implement a function `exists` which tests whether 
a set contains at least one element for which the given predicate is true. 

_Note that the functions forall and exists behave like the universal 
and existential quantifiers of first-order logic._

3. Finally, write a function `map` which transforms a given set 
into another one by applying to each of its elements the given function.

