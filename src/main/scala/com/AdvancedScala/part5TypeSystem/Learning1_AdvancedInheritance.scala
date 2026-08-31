package com.AdvancedScala.part5TypeSystem

object Learning1_AdvancedInheritance {

  // composite type can be used on their own
  trait Writer[T] {
    def write(value : T) : Unit
  }

  trait Stream[T] {
    def foreach(f: T => Unit) : Unit
  }

  trait Closeable {
    def close(status: Int) : Unit
  }

  // class MyDataStream extends Writer[String] with Stream[String] with Closeable {}

  def processStream[T](stream: Writer[T] with Stream[T] with Closeable) : Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem
  trait Animal { def name: String}
  trait Lion extends Animal { override def name: String = "Lion" }
  trait Tiger extends Animal { override def name: String = "Tiger" }
  private class Liger extends Lion with Tiger

  def demoLiger() : Unit = {
    val liger = new Liger
    println(liger.name)
  }

  def main(args: Array[String]): Unit = {
    demoLiger()
  }
}
