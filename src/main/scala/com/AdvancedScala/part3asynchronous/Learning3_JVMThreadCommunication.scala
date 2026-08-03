package com.AdvancedScala.part3asynchronous

import scala.collection.mutable
import scala.util.Random

// Problem : The Producer-Consumer problem

class SimpleContainer {
  private var value: Int = 0
  def isEmpty: Boolean = value == 0
  def set(newValue: Int): Unit = value = newValue
  def get: Int = {
    val result = value
    value = 0
    result
  }
}

// Producer Consumer problem : one producer, one consumer
object ProducerConsumerV1 {
  def start(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting")
      // busy waiting which is very bad
      while (container.isEmpty) {
        println("[consumer] waiting for value")
      }
      println(s"[consumer] I have consumed the value : ${container.get}")
    })

    val producer = new Thread(() => {
      println("[producer] computing..")
      Thread.sleep(500)
      val value = 42
      println(s"[producer] I am producing after long work, the value is $value")
      container.set(value)
    })

    consumer.start()
    producer.start()
  }
}

/*
  Execution flow:

  1. consumer.start()
     → prints "[consumer-2] waiting"
     → enters container.synchronized (acquires the lock on container)
     → calls container.wait()
        → this does TWO things:
          a) RELEASES the lock (so others can use container)
          b) SUSPENDS the consumer thread (it sleeps until notified)

  2. producer.start()
     → prints "[producer-2] computing..."
     → sleeps 500ms (simulating work)
     → enters container.synchronized (acquires the lock — possible because consumer released it via wait())
     → sets the value to 42
     → calls container.notify()
        → this WAKES UP the consumer thread
     → exits synchronized block (releases the lock)

  3. consumer wakes up
     → re-acquires the lock (automatically after being notified)
     → exits synchronized block
     → prints "I have consumed the value: 42"
*/

object ProducerConsumerV2 {
  def start(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer-2] waiting")
      container.synchronized { // block all threads trying to lock this instance
        if(container.isEmpty)
          container.wait() // release the lock + suspend the thread
      }
      println(s"[consumer-2] I have consumed the value : ${container.get}")
    })

    val producer = new Thread(() => {
      println("[producer-2] computing..")
      Thread.sleep(500)
      val value = 42
      container.synchronized {
        println(s"[producer-2] I am producing after long work, the value is $value")
        container.set(value)
        container.notify()  // awaken one suspended thread on this object
      }
    })

    consumer.start()
    producer.start()
  }
}

object ProducerConsumerV3 {
  def start(containerCapacity : Int) : Unit = {
    val buffer : mutable.Queue[Int] = new mutable.Queue[Int]

    val consumer = new Thread(() => {
      val random = new Random(System.nanoTime())
      while(true) {
        buffer.synchronized {
          if(buffer.isEmpty) {
            println("[consumer3] buffer empty waiting..")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"[consumer3] I've just consumed $x")
          buffer.notify()
        }
        Thread.sleep(500)
      }
    })

    val producer = new Thread(() => {
      val random = new Random(System.nanoTime())
      var counter = 0
      while(true) {
        buffer.synchronized {
          if(buffer.size == containerCapacity) {
            println("[producer3] buffer full, waiting...")
            buffer.wait()
          }
          val newElement = counter
          counter += 1
          println(s"[producer3] I'm producing $newElement")
          buffer.enqueue(newElement)
          buffer.notify()
        }
        Thread.sleep(1)
      }
    })

    consumer.start()
    producer.start()

  }
}

object ProducerConsumerV4 {

  class Consumer(id : Int, buffer : mutable.Queue[Int], containerCapacity : Int) extends Thread {
    override def run(): Unit = {
      val random = new Random(System.nanoTime())

      while(true) {
        buffer.synchronized {
          /*
            one producer, two consumers
            producer produces one value in the buffer
            both consumers are waiting
            producer calls notify, awakens one consumer.
            consumer dequeues, calls notify awakens another consumer -- due to scheduling mechanism
            the other consumer awakens, and it will crash because the buffer is empty as we are trying to get an element out
          */

          while(buffer.isEmpty) {
            println(s"[consumer$id] buffer empty waiting")
            buffer.wait()
          }

          val newValue = buffer.dequeue()
          buffer.notifyAll()
          println(s"[consumer$id] consumed $newValue")
        }
        Thread.sleep(random.nextInt(500))
      }

    }
  }

  class Producer(id : Int, buffer : mutable.Queue[Int], containerCapacity : Int) extends Thread {
    override def run(): Unit = {
      val random = new Random(System.nanoTime())
      var currentCount = 0

      while(true) {
        buffer.synchronized {
          while(buffer.size == containerCapacity) {
            println(s"[producer $id] buffer is full..waiting")
            buffer.wait()
          }

          // there is space in the buffer
          println(s"[producer $id] producer producing $currentCount")
          buffer.enqueue(currentCount)

          // wakeup a consumer
          buffer.notifyAll()
          currentCount += 1

        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def start(nProducers : Int, nConsumers : Int, containerCapacity : Int) : Unit = {
    val buffer : mutable.Queue[Int] = new mutable.Queue[Int]
    val producers = (1 to nProducers).map(id => new Producer(id, buffer, containerCapacity))
    val consumers = (1 to nConsumers).map(id => new Consumer(id, buffer, containerCapacity))
    producers.foreach(_.start())
    consumers.foreach(_.start())

  }
}

object Learning3_JVMThreadCommunication {
  def main(args: Array[String]): Unit = {

//    ProducerConsumerV1.start()
//    ProducerConsumerV2.start()
//    ProducerConsumerV3.start(1)
    ProducerConsumerV4.start(1,2,5)

  }
}


