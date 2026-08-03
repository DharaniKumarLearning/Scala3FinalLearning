package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning5_JoinExamples2 {
  def main(args: Array[String]): Unit = {

    // making a child thread wait for main thread using Runnable
    val mainThreadReference = Thread.currentThread()
    val myRunnable = new Runnable {
      override def run(): Unit = {
        mainThreadReference.join()
        (1 to 10).foreach(_ => println("runnable thread"))
      }
    }

    val myThread = new Thread(myRunnable)
    myThread.start()
    (1 to 5).foreach(_ => {Thread.sleep(500); println("main thread executing")})

  }
}
