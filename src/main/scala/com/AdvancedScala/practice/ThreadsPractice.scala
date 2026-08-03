package com.AdvancedScala.practice

class SimpleContainer {
  private var value : Int = 0
  def isEmpty : Boolean = value == 0
  def set(x : Int) : Unit = value = x
  def get : Int = {
    val result = value
    value = 0
    result
  }
}

object ThreadsPractice {

  private def producerConsumerProblemV1() : Unit = {
    val simpleContainer = new SimpleContainer
    val consumer = new Thread(() => {
      simpleContainer.synchronized {
        if(simpleContainer.isEmpty) {
          println("I am waiting for producer to send value")
          simpleContainer.wait()
          println("I got notified by the producer that the value is set hence I am reading it now")
        }
      }
      println(s"I got the value and it is ${simpleContainer.get}")
    })

    val producer = new Thread(() => {
      Thread.sleep(500)
      simpleContainer.synchronized {
        simpleContainer.set(42)
        println("I have set the value for container notifying it now")
        simpleContainer.notify()
      }

      println("producer produced the value to simple container")
    })

    producer.start()
    consumer.start()
    producer.join()
    consumer.join()
  }

  def main(args: Array[String]): Unit = {
    producerConsumerProblemV1()
  }
}
