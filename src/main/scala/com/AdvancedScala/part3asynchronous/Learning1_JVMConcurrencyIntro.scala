package com.AdvancedScala.part3asynchronous

import java.util.concurrent.Executors


object Learning1_JVMConcurrencyIntro {

  def basicThreads() : Unit = {
    val runnable: Runnable = new Runnable {
      override def run(): Unit = {
        println(s"waiting for 2 seconds and the thread name is ${Thread.currentThread().getName}")
        Thread.sleep(2000)
        println("running on some other thread")
      }
    }

    val aThread = new Thread(runnable)
    // aThread.setDaemon(true)  // If we set the thread to daemon the JVM won't wait for the thread to finish
    // If the thread is non-daemon then the JVM will wait until it finishes
    // by default every thread we start is non-daemon

    aThread.start() // will run runnable on some JVM thread.
    // JVM thread == OS thread (soon to change via project loom)
    aThread.join() // join() method can be used to wait for the thread until it finishes
  }

  // order of operations not guaranteed
  // different runs = different results
  def orderOfExecution() : Unit = {
    val threadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))  // single method abstract pattern
    val threadBye = new Thread(() => (1 to 5).foreach(_ => println("GoodBye")))
    threadHello.start()
    threadBye.start()
    threadHello.join()
    threadBye.join()
  }

  // executors
  def demoExecutors() : Unit = {
    val threadPool = Executors.newFixedThreadPool(4)
    // submit a computation
    threadPool.execute(() => println("something in the thread pool"))

    threadPool.execute {() =>
      Thread.sleep(1000)
      println("done after one second")
    }

    threadPool.execute {() =>
      Thread.sleep(1000)
      println("almost done")
      Thread.sleep(1000)
      println("done after 2 seconds")
    }

    threadPool.shutdown()  // without this line the thread pool will still be executing
    // threadPool.execute(() => println("this should not appear")) -- RejectedExecutionException
    threadPool.awaitTermination(Long.MaxValue, java.util.concurrent.TimeUnit.SECONDS)
    // awaitTermination is like join method for threadPool
  }

  def main(args: Array[String]): Unit = {

    basicThreads()
    orderOfExecution()
    demoExecutors()
    println("main thread done")


  }
}
