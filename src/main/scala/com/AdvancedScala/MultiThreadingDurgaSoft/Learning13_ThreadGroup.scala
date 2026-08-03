package com.AdvancedScala.MultiThreadingDurgaSoft

object Learning13_ThreadGroup {
  def main(args: Array[String]): Unit = {

    /**
     * Based on functionality we can group threads into a single unit which is called a ThreadGroup.
     * ThreadGroup can contain a group of threads in addition to threads ThreadGroup can also contain SubThreadGroups
     * The main advantage of maintaining threads in the form of ThreadGroup is we can perform common operations very easily
     * Every thread in Java belongs to some group
     * Every thread group in Java is the child group of system group either directly or indirectly
     * system group contains several system level threads like Finalizer, ReferenceHandler, SignalDispatcher, AttachListener etc.,
     */

    println(s"MainThread group : ${Thread.currentThread().getThreadGroup.getName}")  // main group
    println(s"Main Thread group parent : ${Thread.currentThread().getThreadGroup.getParent.getName}")  // system group

    val threadGroup1 = new ThreadGroup("FirstGroup")  // passing the thread group name
    println(s"threadGroup1 parent : ${threadGroup1.getParent.getName}")  // by default, it belongs to main group

    val threadGroup2 = new ThreadGroup(threadGroup1, "SecondGroup")  // passing the parent thread group and thread group name
    println(s"threadGroup2 parent : ${threadGroup2.getParent.getName}")  // FirstGroup is the parent of the SecondGroup

    /**
     * Important methods of ThreadGroup class
     *  getName -- returns name of the thread group
     *  getParent -- get the parent group of ThreadGroup
     *  list -- prints information about ThreadGroup to the console
     *  activeCount -- returns number of active threads present in the ThreadGroup
     *  activeGroupCount -- returns number of active groups present in the current ThreadGroup
     *  enumerate(t: Array[Thread]) - to copy all active threads of this ThreadGroup into passed thread array in this case sub ThreadGroup threads also will be considered
     *  enumerate(g: Array[ThreadGroup]) -- to copy all sub ThreadGroups into passed ThreadGroup array
     *  isDaemon -- to check whether the ThreadGroup is daemon or not
     *  setDaemon(status:Boolean) -- to change the daemon nature of the ThreadGroup
     *  interrupt -- to interrupt all waiting/sleeping threads present in the ThreadGroup
     *  destroy -- to destroy ThreadGroup and its sub ThreadGroups
     */

    val runnable1 = new Runnable {
      override def run(): Unit = {
        println("Child thread1")
        Thread.sleep(2000)
      }
    }

    val runnable2 = new Runnable {
      override def run(): Unit = {
        println("Child thread2")
        Thread.sleep(2000)
      }
    }

    val pg = new ThreadGroup("ParentGroup")
    val cg = new ThreadGroup(pg, "ChildGroup")
    val t1 = new Thread(pg, runnable1)
    val t2 = new Thread(pg, runnable2)
    t1.start()
    t2.start()
    pg.list()
    println(s"Active thread count in ParentGroup is ${pg.activeCount()}")
    println(s"Active sub ThreadGroup count in ParentGroup is ${pg.activeGroupCount()}")

    val systemThreadGroup = Thread.currentThread().getThreadGroup.getParent
    val systemGroupThreadArray : Array[Thread] = new Array[Thread](systemThreadGroup.activeCount())
    systemThreadGroup.enumerate(systemGroupThreadArray)
    systemGroupThreadArray.foreach(x => println(s"Thread name is ${x.getName}, Is thread daemon? ${x.isDaemon}"))

    Thread.sleep(10000)
    println(s"Active thread count after threads completed in ParentGroup is ${pg.activeCount()}")
    println(s"Active sub ThreadGroup count in ParentGroup after threads completed is ${pg.activeGroupCount()}")


  }
}
