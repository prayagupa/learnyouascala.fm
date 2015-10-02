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
