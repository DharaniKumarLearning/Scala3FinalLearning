package com.AdvancedScala.practice

import java.util.concurrent.Executors
import scala.util.Random

object ThreadsPractice {
  def main(args: Array[String]): Unit = {
    println(s"main thread starting ${Thread.currentThread().getName}")

    val aRunnable : Runnable = new Runnable {
      override def run(): Unit = {
        println(s"This thread will sleep for 2 seconds and the thread name is ${Thread.currentThread().getName}")
        Thread.sleep(1000)
        println(s"This thread is done ${Thread.currentThread().getName}")
      }
    }
    val aThread = new Thread(aRunnable)
    aThread.start()
    aThread.join()

    val helloThread = new Thread(() => (1 to 100).foreach(_ => println("Hello")))
    val byeThread = new Thread(() => (1 to 100).foreach(_ => println("Bye")))
    helloThread.start()
    byeThread.start()
    helloThread.join()
    byeThread.join()

    val threadPool = Executors.newFixedThreadPool(4)
    (1 to 6).foreach(x => threadPool.execute(() => {
      println(s"${Thread.currentThread().getName} sleeping for 1 second with value of x $x")
      Thread.sleep(1000)
      println(s"${Thread.currentThread().getName} is completed with value of x $x")
    }))

    threadPool.shutdown()
    threadPool.awaitTermination(Long.MaxValue, java.util.concurrent.TimeUnit.SECONDS)

    def runInParallel() : Unit = {
      var someVar = 0
      val someThread1 = new Thread(() => {Thread.sleep(1) ; someVar = 1})
      val someThread2 = new Thread(() => {Thread.sleep(1) ; someVar = 2})
      someThread1.start()
      someThread2.start()
      someThread1.join()
      someThread2.join()
      println(s"After threads run the value of someVar is $someVar")
    }

    runInParallel()

    case class BankAccount(var amount : Int)
    def buy(bankAccount: BankAccount, thing : String, price : Int) : Unit = {
      val existing = bankAccount.amount
      val random = new Random()
      Thread.sleep(if(random.nextBoolean()) 1 else 2)
      bankAccount.amount = existing - price
    }

    (1 to 10).foreach { x =>
      val bankAccount = BankAccount(50000)
      val thread1 = new Thread(() => buy(bankAccount, "shoe", 10000))
      val thread2 = new Thread(() => buy(bankAccount, "iPhone", 20000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if(bankAccount.amount != 20000) println(s"race condition occurred for x $x and bank balance is ${bankAccount.amount}")
    }

    def buySafe(bankAccount: BankAccount, thing : String, price : Int) : Unit = {
      bankAccount.synchronized {
        val existing = bankAccount.amount
        val random = new Random()
        Thread.sleep(if (random.nextBoolean()) 1 else 2)
        bankAccount.amount = existing - price
        println(s"${Thread.currentThread().getName} the bank balance is ${bankAccount.amount}")
      }
    }

    (1 to 10).foreach { x =>
      val bankAccount = BankAccount(50000)
      val thread1 = new Thread(() => buySafe(bankAccount, "shoe", 10000))
      val thread2 = new Thread(() => buySafe(bankAccount, "iPhone", 20000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (bankAccount.amount != 20000) println(s"Alert!!! race condition for $x and amount ${bankAccount.amount}")
    }

    println(s"main thread done ${Thread.currentThread().getName}")
  }
}
