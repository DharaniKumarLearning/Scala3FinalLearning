package com.AdvancedScala.practice

import java.util.concurrent.{Callable, ExecutorService, Executors}

object ThreadsPractice {
  def main(args: Array[String]): Unit = {

    val service : ExecutorService = Executors.newFixedThreadPool(4)

    val myCallable = new Callable[String] {
      override def call(): String = {
        println(s"${Thread.currentThread().getName} running")
        Thread.sleep(3000)
        Thread.currentThread().getName + " thread completed"
      }
    }

    (1 to 10).map(_ => service.submit(myCallable)).foreach(y => println(y.get))

    service.shutdown()
  }
}
