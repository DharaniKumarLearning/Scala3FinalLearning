package com.Scala3Essentials

import java.util.concurrent.Executors

object Playground {

  def basicThreads() : Unit = {
    val threadPool = Executors.newFixedThreadPool(5)
    threadPool.execute(() => { Thread.sleep(1000);println(s"first thread ${Thread.currentThread().getName}")})
    threadPool.execute(() => { Thread.sleep(2000);println(s"second thread ${Thread.currentThread().getName}")})
    threadPool.execute(() => { Thread.sleep(3000);println(s"third thread ${Thread.currentThread().getName}")})
    threadPool.execute(() => { Thread.sleep(4000);println(s"fourth thread ${Thread.currentThread().getName}")})
    threadPool.execute(() => { Thread.sleep(5000);println(s"fifth thread ${Thread.currentThread().getName}")})
    threadPool.shutdown()
    val poolTerminated = threadPool.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)
    println(poolTerminated)
  }

  def main(args: Array[String]): Unit = {

    basicThreads()
    println("main method proceeding further")

  }
}
