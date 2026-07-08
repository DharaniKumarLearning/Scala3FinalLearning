package com.Scala3Essentials.DataStructureCreation

abstract class MayBe[A] {
  def map[B](f : A => B) : MayBe[B]
  def filter(predicate : A => Boolean) : MayBe[A]
  def flatMap[B](f : A => MayBe[B]) : MayBe[B]
}

case class MayBeNot[A]() extends MayBe[A] {
  override def map[B](f: A => B): MayBe[B] = MayBeNot[B]()
  override def filter(predicate: A => Boolean): MayBe[A] = this
  override def flatMap[B](f: A => MayBe[B]): MayBe[B] = MayBeNot[B]()
}

case class Just[A](value : A) extends MayBe[A] {
  override def map[B](f: A => B): MayBe[B] = Just(f(value))
  override def filter(predicate: A => Boolean): MayBe[A] = if(predicate(value)) this else MayBeNot[A]()
  override def flatMap[B](f: A => MayBe[B]): MayBe[B] = f(value)
}

object MayBeTest {
  def main(args: Array[String]): Unit = {

    val mayBeInt : MayBe[Int] = Just(3)
    val mayBeInt2 : MayBe[Int] = MayBeNot()
    println(mayBeInt.map(_ + 1))
    println(mayBeInt2.map(_ + 1))
    println(mayBeInt.filter(_ % 2 == 0))
    println(mayBeInt.flatMap(x => Just(x + 1)))

  }
}