package com.AdvancedScala.practice

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object Futures {
  def main(args: Array[String]): Unit = {

    val executors : ExecutorService = Executors.newFixedThreadPool(4)
    given anExecutionContext : ExecutionContext = ExecutionContext.fromExecutorService(executors)

    def calculateMeaningOfLife() : Int = {println(s"${Thread.currentThread().getName} first method is running") ; Thread.sleep(1000) ; throw new RuntimeException("No Int for you")}
    def doubleTheNumber(x : Int) : Int = {println(s"${Thread.currentThread().getName} second method is running") ; Thread.sleep(1000) ; x * 2}

    val aFuture : Future[Int] = Future.apply(calculateMeaningOfLife())(anExecutionContext)

    aFuture.onComplete {
      case Success(value) =>
        println(s"${Thread.currentThread().getName} the future completed and the value is $value")
        val anotherFuture = Future(doubleTheNumber(value))
        anotherFuture.onComplete {
          case Success(finalValue) => println(s"${Thread.currentThread().getName} the second future completed and the value is $finalValue")
          case Failure(exception) => println(s"${Thread.currentThread().getName} second future failed with exception $exception")
        }
      case Failure(ex) => println(s"${Thread.currentThread().getName} Future failed with exception $ex")
    }

    val finalResult = aFuture.flatMap(value => Future(doubleTheNumber(value)))
    val anotherFinalResult = for {
      value <- aFuture
      finalValue <- Future(doubleTheNumber(value))
    } yield finalValue

    val futureMap = aFuture.map(x => x * 3).recover(x => 0)
    val futureMap2 = aFuture.map(x => x * 3).recoverWith(x => Future(45))
    println("Main thread continuing further after calling futures and their callbacks")
    Thread.sleep(5000)
    println(s"finalResult = ${finalResult.value}")
    println(s"anotherFinalResult = ${anotherFinalResult.value}")
    println(s"futureMap result = ${futureMap.value}")
    println(s"futureMap2 result = ${futureMap2.value}")
    executors.shutdown()

  }
}
