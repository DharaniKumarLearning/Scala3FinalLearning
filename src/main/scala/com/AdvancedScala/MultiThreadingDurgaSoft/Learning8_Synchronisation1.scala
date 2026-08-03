package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning8_Synchronisation1 {
  def main(args: Array[String]): Unit = {

    /**
     * Synchronised modifier is applicable only for methods and blocks but not for classes and variables
     * If multiple threads are trying to operate simultaneously on the same java object then we may get data inconsistency problem
     * To overcome this problem we should use synchronised keyword
     * If a method or block declared as synchronised then only one thread is allowed to execute so that data inconsistency problem will be resolved
     * The disadvantage of synchronised keyword is it increases thread waiting time and creates performance problems
     * Internally synchronisation concept is implemented by using lock every object in Java has a unique lock
     * While a thread executing synchronised method on the given object the remaining threads are not allowed to execute any synchronised
     *  method simultaneously on the same object but the remaining threads are allowed to execute non-synchronised methods simultaneously
     * Lock concept is implemented at object level but not at method level
     * Every object has synchronised area (only one thread allowed to access) and non-synchronised area (any number of threads can access it)
     */

    class Display {
      def wish(name: String) : Unit = synchronized {  // this is similar to making a method as synchronised in Java
        (1 to 5).foreach { _ =>
          print("Good Morning : ")
          Thread.sleep(500)
          println(name)
        }
      }

      // even though we have two synchronized methods every thread will get only one object lock
      // hence at a given point of time only one thread will be able to execute either one synchronized method but not both at the same time
      def anotherWish(name: String): Unit = synchronized {
        (1 to 5).foreach { _ =>
          print("Good afternoon : ")
          Thread.sleep(500)
          println(name)
        }
      }
    }

    object Display {
      // When a thread executes static method like this it will acquire class level lock
      def staticWish(name: String) : Unit = synchronized {
        (1 to 5).foreach { _ =>
          print("Good Night : ")
          Thread.sleep(500)
          println(name)
        }
      }
    }

    val display = new Display

    val t1 = new Thread(() => display.wish("Dharani"))
    val t2 = new Thread(() => display.wish("Kavya"))
    val t3 = new Thread(() => display.anotherWish("Dharani"))
    val t4 = new Thread(() => display.anotherWish("Mincy"))

    t1.start()
    t2.start()
    t3.start()
    t4.start()

    t1.join()
    t2.join()
    t3.join()
    t4.join()

    val t5 = new Thread(() => Display.staticWish("Dharani"))
    val t6 = new Thread(() => Display.staticWish("Kavya"))

    t5.start()
    t6.start()
  }
}
