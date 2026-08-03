package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning7_InterruptMethod {
  def main(args: Array[String]): Unit = {

    /**
     * If the target thread not in sleeping state or waiting state then there is no impact of interrupt call immediately
     * interrupt call will be waited until target thread enters sleeping/waiting state
     * If the target thread enters sleeping/waiting state then immediately interrupt call will interrupt target thread
     * If the target thread never entered into sleeping/waiting state in its lifetime then there is no impact of interrupt call
     * This is the only case where interrupt call will get wasted
     */

    val childThread = new Thread(() => {
      (1 to 100000).foreach(x => println(s"child thread $x"))
      println("I am done with the task going to sleep")
      try {
        Thread.sleep(2000)
        println("child thread is done")
      } catch {
        case ex: InterruptedException => println(s"Interruption occurred")
      }
    })

    childThread.start()
    childThread.interrupt()  // not a blocking code main method will continue
    println("End of main")

  }
}
