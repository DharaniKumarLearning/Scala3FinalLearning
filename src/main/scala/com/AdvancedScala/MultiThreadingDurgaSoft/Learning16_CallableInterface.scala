package com.AdvancedScala.MultiThreadingDurgaSoft

import java.util.concurrent.{Callable, ExecutorService, Executors}

object Learning16_CallableInterface {
  def main(args: Array[String]): Unit = {

    /**
     * In the case of Runnable thread won't return anything after completing the job
     * If a thread is required to return some result after execution then we need to use Callable interface.
     * Callable interface contains only one method call()
     * If we submit callable object to executor then after completing the job thread returns an object of the type of
     *  Future i.e. future object can be used to retrieve the result from callable job
     */

    class MyCallable(num: Int) extends Callable[Int] {
      override def call(): Int = (1 to num).sum
    }

    val service : ExecutorService = Executors.newFixedThreadPool(4)

    List(10,20,30,40,50,60).foreach { num =>
      val f = service.submit(MyCallable(num))
      println(s"The sum of first $num numbers is ${f.get()}")
    }
    service.shutdown()
  }
}
