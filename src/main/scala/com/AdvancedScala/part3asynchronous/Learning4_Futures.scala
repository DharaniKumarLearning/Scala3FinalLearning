package com.AdvancedScala.part3asynchronous

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Try,Success,Failure}

object Learning4_Futures {

  // this is thread pool that is java specific
  val executorService : ExecutorService = Executors.newFixedThreadPool(4)

  // this is scala specific thread pool that is wrapper on top of java's thread pool
  given executionContext : ExecutionContext = ExecutionContext.fromExecutorService(executorService)

  def calculateMeaningOfLife() : Int = {
    println(s"This method executed by ${Thread.currentThread().getName}")
    Thread.sleep(1000)
    println(s"I am done")
    42
  }

  // A Future is an asynchronous computation that will finish at some point
  // The argument we pass to apply method will be evaluated on some other thread
  val aFuture : Future[Int] = Future.apply(calculateMeaningOfLife())  // here executionContext is automatically injected by the compiler
  val futureInstantResult : Option[Try[Int]] = aFuture.value // value method will inspect the value of the future RIGHT NOW

  // The type is Option[Try[Int]] because we may or may not have right now that's why Option Try because the computation that is performed on another thread can throw an exception

  // callbacks this will get executed in a different thread in the executionContext once the future completes
  aFuture.onComplete {
    case Success(value) => println(s"I've completed with meaning of line : $value on thread ${Thread.currentThread().getName}")
    case Failure(ex) => println(s"My asynchronous computation failed $ex on thread ${Thread.currentThread().getName}")
  } // here as well executionContext is automatically injected by the compiler

  def main(args: Array[String]) : Unit = {
    Thread.sleep(2000)
    executorService.shutdown()  // once we shut down the executorService no new tasks are allowed to be submitted to thread pool
    // there is shutdownNow() method which will kill the current running threads as well but shutdown() method won't kill the current running threads
  }
}
