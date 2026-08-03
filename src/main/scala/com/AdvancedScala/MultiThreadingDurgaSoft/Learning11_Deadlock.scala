package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning11_Deadlock {
  def main(args: Array[String]): Unit = {

    /**
     * If two threads are waiting for each other forever such type of infinite waiting is called deadlock
     * synchronised keyword is the reason for deadlock
     * There are no resolution techniques for deadlock but several prevention techniques are there
     * 
     */

    class FirstClass {
      def method1(secondClass: SecondClass) : Unit = synchronized {
        println("thread1 acquired first class lock")
        Thread.sleep(500)
        secondClass.last()
      }
      def last() : Unit = synchronized(println("FirstClass last method"))
    }

    class SecondClass {
      def method2(firstClass: FirstClass) : Unit = synchronized {
        println("thread2 acquired second class lock")
        Thread.sleep(500)
        firstClass.last()
      }
      def last() : Unit = synchronized(println("SecondClass last method"))
    }

    val firstClass = new FirstClass
    val secondClass = new SecondClass()
    val thread1 = new Thread(() => firstClass.method1(secondClass))
    val thread2 = new Thread(() => secondClass.method2(firstClass))

    thread1.start()
    thread2.start()

  }
}
