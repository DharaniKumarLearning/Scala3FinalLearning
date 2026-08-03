package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning3_YieldJoinMethods {
  def main(args: Array[String]) : Unit = {

    /**
     * We can prevent a thread execution by using the following methods
     *  1. yield()
     *  2. join()
     *  3. sleep()
     *
     *  yield() method pauses current executing Thread to give chance for waiting threads of same priority.
     *  If there is no waiting thread or all waiting threads have low priority then same thread can continue its execution
     *  If multiple threads are waiting with same priority then we don't know the thread that gets the chance it depends on Thread scheduler
     *  The thread which is yielded we don't know when it will get the chance again depends on Thread scheduler
     *
     *  If a thread wants to wait until the completion of another thread then we need to use join() method
     *  For example if a thread t1 wants to wait until the completion of thread t2 then t1 should call t2.join()
     *  Once t1 executes t2.join() then t1 enters WAITING state until t2 completes t1 enters READY/RUNNABLE state again and proceed further
     *  Once t2 completes then t1 can continue its execution
     *  join() method extends InterruptedException.
     *  In Java, we need to wrap join() method in try/catch because it is checked exception in scala we don't need to handle checked exceptions as well
     *  If a thread calls join method on the same thread itself then the program will get stuck (self-deadlock)
     */

    val myRunnable1 = new Runnable {
      override def run(): Unit = {
        (1 to 5).foreach(_ => {Thread.sleep(1000); println(s"${Thread.currentThread().getName} Sita thread")})
      }
    }

    val myThread = new Thread(myRunnable1)
    myThread.start()
    // myThread.join() -- main thread will wait until myThread completes successfully
    myThread.join(2000) // main thread will only wait for 2 seconds after that it will proceed further and continue its execution
    (1 to 10).foreach(_ => println("Rama thread"))

  }
}
