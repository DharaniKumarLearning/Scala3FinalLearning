package com.AdvancedScala.practice

import scala.annotation.tailrec

// Write a lazily evaluated, potentially infinite linked list
abstract class LazyList[A] {
  def isEmpty : Boolean
  def head : A
  def tail : LazyList[A]

  def #::(element : A) : LazyList[A]  // prepending
  infix def ++(another : => LazyList[A]) : LazyList[A]

  def foreach(f : A => Unit) : Unit
  def map[B](f : A => B) : LazyList[B]
  def flatMap[B](f : A => LazyList[B]) : LazyList[B]
  def filter(predicate : A => Boolean) : LazyList[A]
  def withFilter(predicate : A => Boolean) : LazyList[A] = filter(predicate)

  def take(n : Int) : LazyList[A]  // takes the first n elements from this lazy list
  def takeAsList(n : Int) : List[A] = take(n).toList

  def toList : List[A] = {
    @tailrec
    def toListAux(remaining: LazyList[A], acc : List[A]) : List[A] = {
      if(remaining.isEmpty) acc.reverse
      else toListAux(remaining.tail, remaining.head :: acc)
    }
    toListAux(this, List())
  }

}

case class LzEmpty[A]() extends LazyList[A] {
  override def isEmpty: Boolean = true
  override def head: A = throw new NoSuchElementException()
  override def tail: LazyList[A] = throw new NoSuchElementException()
  override def #::(element: A): LazyList[A] = new LzCons[A](element, this)
  override infix def ++(another: => LazyList[A]): LazyList[A] = another
  override def foreach(f: A => Unit): Unit = ()
  override def map[B](f: A => B): LazyList[B] = LzEmpty[B]()
  override def flatMap[B](f: A => LazyList[B]): LazyList[B] = LzEmpty[B]()
  override def filter(predicate: A => Boolean): LazyList[A] = this
  override def take(n: Int): LazyList[A] = if(n == 0) this else throw new RuntimeException(s"Cannot take $n elements from empty lazy list")
}

class LzCons[A](hd : => A, tl : => LazyList[A]) extends LazyList[A] {
  override def isEmpty: Boolean = false
  override lazy val head: A = hd
  override lazy val tail: LazyList[A] = tl
  override def #::(element: A): LazyList[A] = new LzCons(element, this)
  override infix def ++(another: => LazyList[A]): LazyList[A] = new LzCons(head, tail ++ another)

  override def foreach(f: A => Unit): Unit = {
    @tailrec
    def forEachTailRec(lazyList: LazyList[A]) : Unit = {
      if(lazyList.isEmpty) ()
      else {
        f(lazyList.head)
        forEachTailRec(lazyList.tail)
      }
    }
    forEachTailRec(this)
  }

  override def map[B](f: A => B): LazyList[B] = new LzCons(f(head), tail.map(f))
  override def flatMap[B](f: A => LazyList[B]): LazyList[B] = f(head) ++ tail.flatMap(f)

  override def filter(predicate: A => Boolean): LazyList[A] = {
    if(predicate(head)) new LzCons(head, tail.filter(predicate))
    else tail.filter(predicate)
  }

  override def take(n: Int): LazyList[A] = {
    if(n <= 0) LzEmpty()
    else if(n == 1) new LzCons(head, LzEmpty())
    else new LzCons(head, tail.take(n - 1))
  }
}

object LazyList {

  def empty[A] : LazyList[A] = LzEmpty[A]()
  def generate[A](start : A)(generator : A => A) : LazyList[A] = {
    new LzCons(start, generate(generator(start))(generator))
  }

  def from[A](list : List[A]) : LazyList[A] =
    list.reverse.foldLeft(LazyList.empty) { (currentLazyList, newElement) =>
      new LzCons(newElement, currentLazyList)
    }

  def apply[A](values : A*) : LazyList[A] = from(values.toList)

}

object LazyListPlayGround {
  def main(args: Array[String]): Unit = {

    val naturals = LazyList.generate(1)(x => x + 1)  // Infinite list of natural numbers
    println(naturals.head)
    println(naturals.tail.head)
    println(naturals.tail.tail.head)
    naturals.take(100000).foreach(println)
    println(naturals.take(100000).toList)

    println(naturals.map(_ * 2).takeAsList(100))
    println(naturals.flatMap(x => LazyList(x, x + 1)).takeAsList(100))
    println(naturals.filter(_ < 10).takeAsList(9))
    // println(naturals.filter(_ < 10).takeAsList(10)) -- StackOverflowError

    val combinationLazy = for {
      number <- LazyList(1,2,3)
      string <- LazyList("black", "white")
    } yield s"$number - $string"

    println(combinationLazy.toList)
  }
}
