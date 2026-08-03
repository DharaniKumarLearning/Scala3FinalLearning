package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning10_InterThreadCommunication {
  def main(args: Array[String]): Unit = {

    /**
     * Two threads can communicate with each other by using wait(), notify() and notifyAll() methods
     * The thread which is expecting update needs to call wait() method which will make the thread enter waiting state
     * The thread which is responsible to perform update after performing update it is responsible to call notify() method then
     *  waiting thread will get notification and continue its execution with those updated items
     * wait(), notify() and notifyAll() methods are available in Object class because any thread can call these methods on any java object
     *
     * To call wait(), notify() and notifyAll() methods on any object thread should be owner of that object i.e. the thread should
     *  have lock on that object which means the thread should be in synchronised area
     *  Hence we can call wait(), notify() and notifyAll() methods from synchronised area otherwise we will get RuntimeException saying
     *  java.lang.IllegalMonitorStateException: current thread is not owner
     *
     * If a thread calls wait() method on any object it immediately releases the lock of that particular object and enters into waiting state
     * If a thread calls notify() method on any object it releases the lock of that object but may not be immediately
     * Except wait(), notify() and notifyAll() there is no other method where thread releases the lock
     *
     * We can use notifyAll() to give notification to all waiting threads of a particular object
     * Event though multiple threads are notified but execution will be performed by one thread at a time as threads require lock on the object
     */

    var x = 0
    val myThread = new Thread(() => {
      synchronized {
        println("Child thread updating the value of x")
        (1 to 100).foreach(y => x = x + y)
        println("Child thread done with updating the value sending notification")
        notify()
      }
    })

    myThread.start()
    // Thread.sleep(2000) -- If we add this line then main thread will sleep for sometime by that time child thread will be completed and main thread will wait forever
    myThread.synchronized {
      println("Main thread waiting for child thread update")
      myThread.wait()
      println(s"Main thread got notification and the value of x is $x")
    }


  }
}
