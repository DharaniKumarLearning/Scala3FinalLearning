package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning9_Synchronisation2 {
  def main(args: Array[String]): Unit = {

    /**
     * If very few lines of the code require synchronisation then it is not recommended to declare entire method as synchronised
     * We have to enclose those few lines of the code by using synchronised block
     * The main advantage of synchronised block over synchronised method is it reduces waiting time of threads and improves performance
     * A thread can acquire multiple locks of different objects
     */

    class X
    class Y

    class Z {
      def m1() : Unit = synchronized {
        println("Thread acquired instance level lock of Z class")
        Thread.sleep(500)
        val x = new X
        x.synchronized {
          println("Thread now has locks of both X and Z class instances")
          Thread.sleep(500)
          val y = new Y
          y.synchronized {
            Thread.sleep(500)
            println("Thread now has locks of X, Y and Z")
          }
        }
      }
    }

    val z = new Z
    val z1 = new Z
    val t1 = new Thread(() => z.m1())
    val t2 = new Thread(() => z1.m1())

    t1.start()
    t2.start()
  }
}
