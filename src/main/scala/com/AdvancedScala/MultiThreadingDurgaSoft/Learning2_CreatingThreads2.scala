package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning2_CreatingThreads2 {
  def main(args: Array[String]): Unit = {

    /**
     * We can define a Thread by implementing Runnable interface.
     * Runnable interface is present in java.lang package, and it contains only one method run()
     * It is always recommended to create Threads from Runnable interface rather than extending the Thread class
     */

    val myRunnable = new Runnable {
      override def run(): Unit = {
        println(s"This thread priority : ${Thread.currentThread().getPriority}")
        (1 to 10).foreach(_ => println(s"${Thread.currentThread().getName} child thread"))
      }
    }

    val myThread = new Thread(myRunnable)
    myThread.start()
    println(s"The name of the thread that got started is : ${myThread.getName}")
    (1 to 10).foreach(_ => println(s"${Thread.currentThread().getName} main thread"))

    /**
     * Some case studies
     *  val myRunnable = new Runnable { override def run() : Unit = ??? }
     *  val myThread1 = new Thread()
     *  val myThread2 = new Thread(myRunnable)
     *
     * case 1: myThread1.start() -- new Thread will get created which is responsible for the execution of Thread class run() method
     * case 2: myThread1.run() -- no new Thread will be created and Thread class run method will be executed just like a normal method call
     * case 3: myThread2.start() -- new Thread will be created which is responsible for the execution of Runnable class run() method
     * case 4: myThread2.run() -- no new Thread will be created and Runnable run() method will be executed just like a normal method call
     * case 5: myRunnable.start() -- We will get compile time error saying Runnable doesn't have start method like that
     * case 6: myRunnable.run() -- no new Thread will be created and Runnable run() method will be executed just like a normal method call
     */

    /**
     * Thread class constructors
     * Thread() -- default constructor
     * Thread(Runnable) -- passing a runnable
     * Thread(name: String) -- giving thread a name
     * Thread(Runnable, name: String) -- giving thread a name while taking Runnable
     * Thread(ThreadGroup, name: String) -- assigning a thread to a threadGroup and giving it a name
     * Thread(ThreadGroup, Runnable) -- assigning a thread to a threadGroup and taking Runnable
     * Thread(ThreadGroup, Runnable, name: String) -- assigning a thread to a threadGroup and taking Runnable and also setting a name
     * Thread(ThreadGroup, Runnable, name: String, stackSize: Long) -- assigning a thread to a threadGroup and taking Runnable and also setting a name and also setting the stack size
     */

    val threadGroup = new ThreadGroup("myThreadGroup")
    val thread1 = new Thread()
    val thread2 = new Thread(myRunnable)
    val thread3 = new Thread("myThread3")
    val thread4 = new Thread(myRunnable, "myThread4")
    val thread5 = new Thread(threadGroup, "myThread5")
    val thread6 = new Thread(threadGroup, myRunnable)
    val thread7 = new Thread(threadGroup, myRunnable, "myThread7")
    val thread8 = new Thread(threadGroup, myRunnable, "myThread8", 100L)

    println(s"The current executing thread name is : ${Thread.currentThread().getName}")
    Thread.currentThread().setName("Not main thread")
    println(s"The current executing thread name after changing it is : ${Thread.currentThread().getName}")

    /**
     * Every thread in Java has some priority between 1 and 10 where 1 is min and max is 10
     * Thread.MIN_PRIORITY -> 1
     * Thread.NORM_PRIORITY -> 5
     * Thread.MAX_PRIORITY -> 10
     * The default priority of main thread is 5
     */

    println(s"Thread min priority = ${Thread.MIN_PRIORITY}")
    println(s"Thread max priority = ${Thread.MAX_PRIORITY}")
    println(s"Thread norm priority = ${Thread.NORM_PRIORITY}")
    println(s"The main thread priority is : ${Thread.currentThread().getPriority}")

  }
}
