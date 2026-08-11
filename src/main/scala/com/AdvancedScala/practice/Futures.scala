package com.AdvancedScala.practice

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Futures {
  def main(args: Array[String]): Unit = {

    val executors : ExecutorService = Executors.newFixedThreadPool(5)
    given anExecutionContext : ExecutionContext = ExecutionContext.fromExecutorService(executors)

    val result : Future[Int] = Future(10).filter(_ > 20).recoverWith(_ => Future(20))
    Thread.sleep(1000)
    println(result.value)

    executors.shutdown()

  }
}
