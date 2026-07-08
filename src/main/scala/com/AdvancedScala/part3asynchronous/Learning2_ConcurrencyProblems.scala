package com.AdvancedScala.part3asynchronous

object Learning2_ConcurrencyProblems {

  private def runInParallel() : Unit = {
    var x = 0
    val thread1 = new Thread(() => x = 1)
    val thread2 = new Thread(() => x = 2)
    thread1.start()
    thread2.start()
    println(x)  // race condition
  }

  case class BankAccount(var amount: Int)
  private def buy(account: BankAccount, thing: String, price: Int) : Unit = {
    val current = account.amount
    Thread.sleep(1)
    account.amount = current - price
  }

  // Race condition
  private def demoBankingProblem() : Unit = {
    (1 to 1000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoe", 3000))
      val thread2 = new Thread(() => buy(account, "clothes", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if(account.amount != 43000) println(s"AHA I have just broken the bank : ${account.amount}")
    }
  }

  private def buySafe(account: BankAccount, thing: String, price: Int) : Unit = {
    account.synchronized {  // synchronized doesn't allow multiple threads to run the critical section
      val current = account.amount
      Thread.sleep(1)
      account.amount = current - price  // critical section
    }
  }

  private def demoBankingProblemSynchronized(): Unit = {
    (1 to 1000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buySafe(account, "shoe", 3000))
      val thread2 = new Thread(() => buySafe(account, "clothes", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) println(s"AHA I have just broken the bank : ${account.amount}")
    }
  }

  /*
    Exercises
      1 - Create "inception threads"
          thread1 creates thread2
            --> thread2 creates thread3
              --> thread3 created threadN ...
          each thread prints "hello from thread $i"
          Print all messages in Reverse Order

      2 - What's the maximum/minimum value of x
      3 - "sleep fallacy"
  */

  private def inceptionThreads(maxthreads : Int, i: Int = 1) : Thread =
    new Thread(() => {
      if(i < maxthreads) {
        val newThread = inceptionThreads(maxthreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from thread $i")
    })

  private def minMaxX() : Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
    println(s"The value of x is $x")
  }

  private def demoSleepFallacy() : Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })
    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    println(s"message = $message")
  }

  def main(args: Array[String]): Unit = {
    runInParallel()
    // demoBankingProblem() -- this will give race condition
    demoBankingProblemSynchronized()
    inceptionThreads(50).start()
    minMaxX()
    demoSleepFallacy()

  }
}
