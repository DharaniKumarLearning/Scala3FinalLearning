package com.AdvancedScala.MultiThreadingDurgaSoft

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

object Learning14_JavaUtilConcurrent {
  def main(args: Array[String]): Unit = {

    /**
     * Traditional synchronized keyword doesn't provide alternatives when the lock on the object is not readily available
     *
     * java.util.concurrent.locks package has some method like
     *  1. lock() -- this method is same as synchronized keyword will wait forever until it gets lock
     *  2. tryLock() -- this method will try to acquire the lock on the object if it doesn't then it will proceed with alternatives
     *  3. tryLock(2, TimeUnit.HOURS) it will wait for certain time until the lock is available otherwise it will proceed with alternative execution
     *  4. unlock() -- to release the lock. If we call this method without acquiring the lock we get IllegalMonitorStateException
     *
     *  TimeUnit is an enum present in java.util.concurrent package
     *    NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS
     *
     *  ReentrantLock -- It is the implementation class of Lock interface
     *  Reentrant means a thread can acquire same lock multiple times internally reentrant lock increments thread's personal hold count whenever we call lock methods and decrements hold count
     *    whenever we call unlock method and releases the lock when the hold count reaches zero
     *
     * Important methods of Reentrant lock
     *  int getHoldCount -- returns the number of holds the current thread has
     *  boolean isHeldByCurrentThread -- is the lock held by current thread?
     *  int getQueueLength -- returns the number of threads waiting to get the lock
     *  Collection getQueuedThreads -- returns a collection of threads that are in queue to acquire the lock
     *  boolean hasQueuedThreads -- are there any waiting threads to acquire the lock?
     *  boolean isLocked -- whether someone has acquired this lock or not
     *  boolean isFair -- is the lock fair or not
     *  Thread getOwner() -- returns the thread who acquired the lock
     */

    val l = new ReentrantLock()
    val l1 = new ReentrantLock(true)  // the parameter we passed is fairness -- fairness means longest waiting thread will get chance

    l.lock()
    l.lock()
    println(s"Is reentrant lock locked : ${l.isLocked}")
    println(s"Is lock held by current thread : ${l.isHeldByCurrentThread}")
    println(s"Are there threads waiting to get the lock : ${l.getQueueLength}")
    l.unlock()
    println(s"Current thread has ${l.getHoldCount} holds on this lock")
    println(s"Is reentrant lock still locked : ${l.isLocked}")
    l.unlock()
    println(s"Is reentrant lock locked: ${l.isLocked}")
    println(s"Is the reentrant lock fair: ${l.isFair}")

    class ReentrantLockDemo {
      val reentrantLock = new ReentrantLock()
      def someMethod(name:String) : Unit =  {
        reentrantLock.lock()
        (1 to 10).foreach { _ =>
          print("Good morning : ")
          Thread.sleep(100)
          println(name)
        }
        reentrantLock.unlock()
      }
    }

    val reentrantLockDemo = new ReentrantLockDemo
    val t1 = new Thread(() => reentrantLockDemo.someMethod("Dharani"))
    val t2 = new Thread(() => reentrantLockDemo.someMethod("Kavya"))
    t1.start()
    t2.start()

    val newRunnable = new Runnable {
      override def run(): Unit = {
        if(l.tryLock()) {  // tryLock() method acquires lock if it is free
          println("I got the lock executing happily")
          Thread.sleep(500)
          l.unlock()
        } else {
          println("I didn't get the lock hence executing the code which doesn't require lock")
          Thread.sleep(500)
        }
      }
    }

    val t3 = new Thread(newRunnable)
    val t4 = new Thread(newRunnable)
    t3.start()
    t4.start()
    
  }
}
