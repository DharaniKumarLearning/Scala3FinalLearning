package com.AdvancedScala.practice

import scala.annotation.tailrec

// isEmpty, head, tail, #::, ++, foreach, map, flatMap, filter, withFilter, take, takeAsList, toList

abstract class DharaniList[A] {
  def isEmpty : Boolean
  def head : A
  def tail : DharaniList[A]
  def #::(elem : A) : DharaniList[A]
  def ++(anotherList : => DharaniList[A]) : DharaniList[A]
  def foreach(f : A => Unit) : Unit
  def map[B](f : A => B) : DharaniList[B]
  def flatMap[B](f : A => DharaniList[B]) : DharaniList[B]
  def filter(f : A => Boolean) : DharaniList[A]
  def withFilter(f : A => Boolean) : DharaniList[A] = filter(f)
  def take(n : Int) : DharaniList[A]
  def takeAsList(n : Int) : List[A] = take(n).toList
  def toList : List[A]
}

case class EmptyDharaniList[A]() extends DharaniList[A] {
  override def isEmpty: Boolean = true
  override def head: A = throw new NoSuchElementException
  override def tail: DharaniList[A] = throw new NoSuchElementException
  override def #::(elem: A): DharaniList[A] = new NonEmptyDharaniList[A](elem, this)
  override def ++(anotherList: => DharaniList[A]): DharaniList[A] = anotherList
  override def foreach(f: A => Unit): Unit = ()
  override def map[B](f: A => B): DharaniList[B] = EmptyDharaniList[B]()
  override def flatMap[B](f: A => DharaniList[B]): DharaniList[B] = EmptyDharaniList[B]()
  override def filter(f: A => Boolean): DharaniList[A] = this
  override def take(n: Int): DharaniList[A] = if(n == 0) this else throw new RuntimeException("Cannot take from empty list")
  override def toList: List[A] = List()
}

class NonEmptyDharaniList[A](hd : => A, tl : => DharaniList[A]) extends DharaniList[A] {
  override def isEmpty: Boolean = false
  override lazy val head : A = hd
  override lazy val tail : DharaniList[A] = tl
  override def #::(elem: A): DharaniList[A] = new NonEmptyDharaniList[A](elem, this)
  override def ++(anotherList: => DharaniList[A]): DharaniList[A] = new NonEmptyDharaniList[A](head, tail ++ anotherList)

  override def foreach(f: A => Unit): Unit = {
    @tailrec
    def foreachHelper(dharaniList: DharaniList[A]): Unit = {
      if (dharaniList.isEmpty) ()
      else {
        f(dharaniList.head)
        foreachHelper(dharaniList.tail)
      }
    }
    foreachHelper(this)
  }

  override def map[B](f: A => B): DharaniList[B] = new NonEmptyDharaniList[B](f(head), tail.map(f))
  override def flatMap[B](f: A => DharaniList[B]): DharaniList[B] = f(head) ++ tail.flatMap(f)
  override def filter(f: A => Boolean): DharaniList[A] =
    if(f(head)) new NonEmptyDharaniList[A](head, tail.filter(f))
    else tail.filter(f)

  override def take(n: Int): DharaniList[A] = {
    if(n == 0) EmptyDharaniList()
    else if(n == 1) new NonEmptyDharaniList[A](head, EmptyDharaniList())
    else new NonEmptyDharaniList[A](head, tail.take(n - 1))
  }

  override def toList: List[A] = {
    @tailrec
    def toListHelper(dharaniList: DharaniList[A], acc: List[A]): List[A] = {
      if(dharaniList.isEmpty) acc
      else toListHelper(dharaniList.tail, acc :+ dharaniList.head)
    }
    toListHelper(this, List.empty)
  }
}

object DharaniList {

  def generate[A](start : A)(generator : A => A) : DharaniList[A] =
    new NonEmptyDharaniList[A](generator(start), generate(generator(start))(generator))

  def apply[A](values : A*) : DharaniList[A] = {
    @tailrec
    def applyHelper(remaining : List[A], acc : DharaniList[A]) : DharaniList[A] = {
      if(remaining.isEmpty) acc
      else applyHelper(remaining.tail, remaining.head #:: acc )
    }
    applyHelper(values.toList.reverse, EmptyDharaniList())
  }

}

object NonEmptyDharaniListTest {
  def main(args: Array[String]): Unit = {

    val naturals = DharaniList.generate(0)(x => x + 1)
    println(naturals.take(10).toList)
    println(naturals.filter(_ > 10).take(20).toList)
    println(naturals.map(_ * 10).filter(_ > 20).take(10).toList)
    println(naturals.flatMap(x => DharaniList(x, x + 1)).take(10).toList)
    println((naturals ++ DharaniList(10,20)).take(20).toList)

  }
}
