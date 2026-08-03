package com.AdvancedScala.MultiThreadingDurgaSoft

import java.util.concurrent.{Callable, ExecutorService, Executors}

object Learning15_ThreadPoolsExecutorFramework {
  def main(args: Array[String]): Unit = {

    /**
     * Creating a new thread for every job may create performance and memory problems
     * To overcome thi we should go for thread pool.
     * Thread pool is a pool of already created threads that are ready to do our job
     * java 1.5 introduces thread pool framework to implement thread pools
     * Thread pool is also known as executor framework
     */

    val service: ExecutorService = Executors.newFixedThreadPool(4)

    class MyRunnable(name:String) extends Runnable {
      override def run(): Unit = {
        println(s"This runnable started by some thread in thread pool ${Thread.currentThread().getName} and the value passed is $name")
        Thread.sleep(5000)
        println(s"This runnable ended by some thread in thread pool ${Thread.currentThread().getName}")
      }
    }

    List("Kavya", "Dharani", "Mincy", "John", "Ravi", "Somu").foreach(name => service.submit(MyRunnable(name)))  // submitting a job to thread pool
    service.shutdown() // we can shut down executor service by calling shutdown() method
    

  }
}
