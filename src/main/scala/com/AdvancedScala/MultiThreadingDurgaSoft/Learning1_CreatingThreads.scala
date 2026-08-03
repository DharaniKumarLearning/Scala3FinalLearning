package com.AdvancedScala.MultiThreadingDurgaSoft

class MyThread extends Thread { // creating a Thread by extending Thread class
  override def run(): Unit = {
    (1 to 10).foreach(_ => println(s"${Thread.currentThread().getName} Child Thread"))
  }
  def run(aString: String) : Unit = println(aString) // we can overload run method
}

class MyThread2 extends Thread {
  override def start(): Unit = println("start method")
  override def run(): Unit = println("run method")
}

object Learning1_CreatingThreads {
  def main(args: Array[String]): Unit = {
    val myThread = new MyThread
    myThread.start()

    /**
     * Thread class start method will get executed in the above code which will create new Thread which will call the run() method of our MyThread class
     * If we do myThread.run() in this case no new thread will get created it will be a normal method call
     * Thread class start() method is responsible to register the thread with Thread Scheduler and all other mandatory activities
     * Hence without calling the start() method of thread class there is no way of creating Threads
     * Overloading of run() method is possible but Thread class will always call no argument run() method
     * If we don't override run() method of Thread class then run() method of Thread class will get executed which won't give any output
     */

    (1 to 10).foreach(_ => println(s"${Thread.currentThread().getName} Main Thread"))
    myThread.run("Dharani")
    // myThread.start() -- if we start the thread again we get java.lang.IllegalThreadStateException

    val myThread2 = new MyThread2
    myThread2.start()  // this call won't create new thread because we are not calling Thread class start() method this will get executed by main thread like a normal method call

    /**
     * Thread life cycle
     * val myThread = new MyThread -- thread enters NEW/BORN state
     * myThread.start() -- Thread enters READY/RUNNABLE state
     * If Thread scheduler allocates processor to Thread it enters RUNNING state
     * If run() method completes the Thread enters DEAD state
     */

  }
}
