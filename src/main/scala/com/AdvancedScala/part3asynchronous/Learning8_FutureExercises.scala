package com.AdvancedScala.part3asynchronous

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Random, Try}

object Learning7_FutureExercises {
  def main(args: Array[String]): Unit = {

    /**
     * Exercises
     * 1) fulfil a future immediately with a value
     * 2) inSequence : make sure the first future has been completed before returning the second
     * 3) first(fa, fb) => new Future with the value of the first Future to complete
     * 4) last(fa, fb) => new Future with the value of the last Future to complete
     * 5) retry an action returning a Future until a predicate holds true
     */

    val executors : ExecutorService = Executors.newFixedThreadPool(4)
    given executionContext : ExecutionContext = ExecutionContext.fromExecutorService(executors)

    def completeImmediately[A](value : A) : Future[A] = Future(value)  // async completion as soon as possible
    def completeImmediately_v2[A](value : A) : Future[A] = Future.successful(value)  // synchronous completion

    def inSequence[A, B](first : Future[A], second: Future[B]) : Future[B] = first.flatMap(_ => second)

    def first[A](f1 : Future[A], f2 : Future[A]) = {
      val promise = Promise[A]()
      f1.onComplete(result1 => promise.tryComplete(result1))
      f2.onComplete(result2 => promise.tryComplete(result2))  // if we call complete on an already completed promise we will get error
      promise.future
    }

    def last[A](f1 : Future[A], f2 : Future[A]) = {
      val bothPromise = Promise[A]()
      val lastPromise = Promise[A]()

      def checkComplete(result: Try[A]) : Unit = {
        if(!bothPromise.tryComplete(result))
          lastPromise.complete(result)
      }

      f1.onComplete(checkComplete)
      f2.onComplete(checkComplete)
      lastPromise.future
    }

    def retryUntil[A](action : () => Future[A], predicate : A => Boolean) : Future[A] =
      action()
        .filter(predicate)
        .recoverWith {
          case _ => retryUntil(action, predicate)
        }

    lazy val fast = Future {
      Thread.sleep(100)
      1
    }

    lazy val slow = Future {
      Thread.sleep(500)
      2
    }

    first(fast, slow).foreach(result => println(s"FIRST: $result"))
    last(fast, slow).foreach(result => println(s"LAST: $result"))

    def testRetries() : Unit = {
      val random = new Random()
      val action = () => Future {
        Thread.sleep(100)
        val nextValue = random.nextInt(100)
        println(s"Generated $nextValue")
        nextValue
      }
      val predicate = (x : Int) => x < 10
      retryUntil(action,predicate).foreach(finalResult => println(s"Settled at $finalResult"))
    }

    testRetries()

    Thread.sleep(10000)
    executors.shutdown()
  }
}
