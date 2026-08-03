package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning4_JoinExamples {

  class MyThread(mainThreadReference: Thread) extends Thread {
    override def run(): Unit = {
      mainThreadReference.join()
      (1 to 10).foreach(_ => println("child thread"))
    }
  }

  def main(args: Array[String]): Unit = {

    // making a child thread wait for main thread by extending Thread class
    val myThread = new MyThread(Thread.currentThread())
    myThread.start()
    (1 to 5).foreach(_ => {Thread.sleep(500);println("main thread ")})

  }
}
