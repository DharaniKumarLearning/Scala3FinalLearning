package com.AdvancedScala.part3asynchronous

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Success, Failure}

object Learning6_Promises {
  def main(args: Array[String]): Unit = {

    val executors : ExecutorService = Executors.newFixedThreadPool(4)
    given executionContext : ExecutionContext = ExecutionContext.fromExecutorService(executors)

    val promise = Promise[Int]()
    val futureInside : Future[Int] = promise.future

    // thread1 - consumer monitor the future for completion
    futureInside.onComplete {
      case Success(value) => println(s"I've just been completed with $value")
      case Failure(exception) => exception.printStackTrace()
    }

    // thread2 -- producer
    val producerThread = new Thread(() => {
      println("crunching numbers")
      Thread.sleep(1000)
      // fulfil the promise
      promise.success(42)
      println("I'm done")
    })

    producerThread.start()
    Thread.sleep(2000)
    executors.shutdown()

  }
}
