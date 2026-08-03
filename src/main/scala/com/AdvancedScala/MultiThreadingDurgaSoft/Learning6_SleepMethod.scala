package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning6_SleepMethod {
  def main(args: Array[String]): Unit = {

    /**
     * If a thread doesn't want to perform any operation for a particular amount of time then we should use sleep method
     * Once we call sleep() method the Thread will enter SLEEPING state
     * If time expires or if the sleeping thread got interrupted the thread will enter READY/RUNNABLE state
     * A thread can interrupt a sleeping thread/waiting thread by using interrupt method of Thread class
     */

    val sleepThread = new Thread(() => {
      try {
        (1 to 5).foreach { _ =>
          println("I am a lazy thread")
          Thread.sleep(500)
        }
      } catch {
        case ex: InterruptedException => println("got interrupted exception")
      }
    })

    sleepThread.start()
    sleepThread.interrupt()
    println("main thread completed")


  }
}
