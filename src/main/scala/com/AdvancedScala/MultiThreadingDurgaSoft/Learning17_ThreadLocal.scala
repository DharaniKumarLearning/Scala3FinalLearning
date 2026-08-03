package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning17_ThreadLocal {
  def main(args: Array[String]): Unit = {

    /**
     * ThreadLocal provides thread local variables.
     * ThreadLocal class maintains values per thread basis
     * Each thread local object maintains separate value like userId, transactionId etc., for each thread that access that object
     * Thread can access its local value, can manipulate its value and even can remove its value
     */

    val threadLocal = new ThreadLocal[String] {
      override def initialValue(): String = "Kavya"  // setting the initial value
    }
    println(threadLocal.get())
    threadLocal.set("Dharani")
    println(threadLocal.get())
    threadLocal.remove()
    println(threadLocal.get())  // by default, it calls initialValue method internally

    var incrementer = 0
    val threadLocalInt = new ThreadLocal[Int] {
      override def initialValue(): Int = {
        incrementer += 1
        incrementer}
    }

    val threadLocalRunnable : Runnable = () => {
      println(s"${Thread.currentThread().getName} got the incrementer value a ${threadLocalInt.get()}")
    }

    val t1 = new Thread(threadLocalRunnable)
    val t2 = new Thread(threadLocalRunnable)
    val t3 = new Thread(threadLocalRunnable)
    val t4 = new Thread(threadLocalRunnable)
    t1.start()
    t2.start()
    t3.start()
    t4.start()

    // by default, ParentThread local variable won't be available to child thread
    class ParentThread extends Thread {
      override def run(): Unit = {
        ParentThread.parentThreadLocal.set(4.0)
        val childThread = new ChildThread
        childThread.start()
        println(s"Parent thread value -- ${ParentThread.parentThreadLocal.get()}")
      }
    }

    class ChildThread extends Thread {
      override def run(): Unit = {
        println(s"Child thread value -- ${ParentThread.parentThreadLocal.get()}")
      }
    }

    object ParentThread {
      // If we use InheritableThreadLocal then the child thread will get ParentThread ThreadLocal variable
      val parentThreadLocal: ThreadLocal[Double] = new ThreadLocal[Double] {
        override def initialValue(): Double = 1.0
      }
    }

    val parentThread = new ParentThread
    parentThread.start()
  }
}
