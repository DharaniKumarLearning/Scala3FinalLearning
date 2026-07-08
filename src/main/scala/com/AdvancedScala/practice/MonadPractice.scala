package com.AdvancedScala.practice

import scala.annotation.targetName

case class Lazy[A](value : () => A) {
  def map[B](f : A => B) : Lazy[B] = Lazy[B](() => f(value()))
  def flatMap[B](f : A => Lazy[B]) : Lazy[B] = Lazy[B](() => f(value()).value())
}

object Lazy {
  @targetName("pure")
  def apply[A](value : => A) : Lazy[A] = new Lazy[A](() => value)
}

object MonadPractice {
  def main(args: Array[String]): Unit = {

    val lazy1 = Lazy { println("computing 1"); 10 }
    val lazy2 = Lazy { println("computing 2"); 20 }

    val result = for {
      a <- lazy1
      b <- lazy2
    } yield a + b

    println("before value")
    println(result.value())
    println(result.value())

    val f = (x : Int) => Lazy(x + 10)
    val pure = (x : Int) => Lazy(x)
    val leftIdentity = pure(44).flatMap(f).value() == f(44).value()
    println(leftIdentity)

  }
}
