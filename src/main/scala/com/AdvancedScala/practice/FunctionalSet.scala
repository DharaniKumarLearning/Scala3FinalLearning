package com.AdvancedScala.practice

import scala.annotation.tailrec

abstract class FSet[A] extends (A => Boolean) {
  def contains(elem : A) : Boolean
  def apply(elem : A) : Boolean = contains(elem)

  infix def +(elem : A) : FSet[A]
  infix def ++(anotherSet : FSet[A]) : FSet[A]

  def map[B](f : A => B) : FSet[B]
  def flatMap[B](f : A => FSet[B]) : FSet[B]
  def filter(f : A => Boolean) : FSet[A]
  def foreach(f : A => Unit) : Unit

  infix def -(elem : A) : FSet[A]
  infix def --(anotherSet : FSet[A]) : FSet[A]
  infix def &(anotherSet : FSet[A]) : FSet[A]
  
}

case class Empty[A]() extends FSet[A] {
  override def contains(elem: A): Boolean = false
  override infix def +(elem: A): FSet[A] = NonEmpty(elem, this)
  override infix def ++(anotherSet: FSet[A]): FSet[A] = anotherSet
  override def map[B](f: A => B): FSet[B] = Empty[B]()
  override def flatMap[B](f: A => FSet[B]): FSet[B] = Empty[B]()
  override def filter(f: A => Boolean): FSet[A] = this
  override def foreach(f: A => Unit): Unit = ()

  override infix def -(elem: A): FSet[A] = this
  override infix def --(anotherSet: FSet[A]): FSet[A] = this
  override infix def &(anotherSet: FSet[A]): FSet[A] = this
}

case class NonEmpty[A](head : A, tail : FSet[A]) extends FSet[A] {
  override infix def +(elem: A): FSet[A] = if(contains(elem)) this else NonEmpty(elem, this)
  override infix def ++(anotherSet: FSet[A]): FSet[A] = tail ++ anotherSet + head
  override def map[B](f: A => B): FSet[B] = tail.map(f) + f(head)
  override def flatMap[B](f: A => FSet[B]): FSet[B] = tail.flatMap(f) ++ f(head)

  override infix def -(elem: A): FSet[A] =
    if(head != elem) tail - elem + head
    else tail - elem

  override infix def --(anotherSet: FSet[A]): FSet[A] = filter(x => !anotherSet(x))
  override infix def &(anotherSet: FSet[A]): FSet[A] = filter(anotherSet)

  override def foreach(f: A => Unit): Unit =
    f(head)
    tail.foreach(f)

  override def contains(elem: A): Boolean =
    head == elem || tail.contains(elem)

  override def filter(f: A => Boolean): FSet[A] =
    val filteredTail = tail.filter(f)
    if f(head) then filteredTail + head  // Scala 3 if then syntax
    else filteredTail
}

object FSet {
  def apply[A](values : A*) : FSet[A] = {
    @tailrec
    def buildSet(valuesSeq : Seq[A], acc : FSet[A]) : FSet[A] = {
      if(valuesSeq.isEmpty) acc
      else buildSet(valuesSeq.tail, acc + valuesSeq.head)
    }
    buildSet(values, Empty())
  }
}

object FunctionalSet {
  def main(args: Array[String]): Unit = {

    val first5 = FSet(1, 2, 3, 4, 5)
    val someNumbers = FSet(4,5,6,7,8)
    println(first5.contains(5))
    println(first5.contains(50))
    println(first5(6)) // calls apply method
    println((first5 + 10).contains(10))
    println(first5.map(_ * 2).contains(10))
    println(first5.map(_ % 2).contains(1))
    println(first5.flatMap(x => FSet(x, x + 1)).contains(7))
    println(first5.flatMap(x => FSet(x, x + 1)).contains(6))

    val aSet = Set(1,2,3)
    val aList = (1 to 10).toList
    println(aList.filter(aSet))
    // aList.filter((x: Int) => aSet.apply(x)) --> aList.filter((x: Int) => aSet.contains(x))

    println((first5 - 3).contains(3))
    println((first5 -- someNumbers).contains(4))
    println((first5 & someNumbers).contains(4))

  }
}
