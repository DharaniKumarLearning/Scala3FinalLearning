package com.Scala3Essentials.DataStructureCreation

import scala.annotation.tailrec

abstract class LinkedList[A] {
  def head : A
  def tail : LinkedList[A]
  def isEmpty : Boolean
  def add(element : A) : LinkedList[A]
  def map[B](transform : A => B) : LinkedList[B]
  def filter(predicate : A => Boolean) : LinkedList[A]
  def withFilter(predicate : A => Boolean) : LinkedList[A] = filter(predicate)
  def ++(anotherList : LinkedList[A]) : LinkedList[A]
  def flatMap[B](transform : A => LinkedList[B]) : LinkedList[B]
}

case class EmptyLinkedList[A]() extends LinkedList[A] {
  override def head : A = throw new NoSuchElementException
  override def tail : LinkedList[A] = throw new NoSuchElementException
  override def isEmpty: Boolean = true
  override def add(element: A): LinkedList[A] = NonEmptyLinkedList[A](element, this)
  override def ++(anotherList: LinkedList[A]): LinkedList[A] = anotherList
  override def map[B](transform: A => B): LinkedList[B] = EmptyLinkedList[B]()
  override def filter(predicate: A => Boolean): LinkedList[A] = this
  override def flatMap[B](transform: A => LinkedList[B]): LinkedList[B] = EmptyLinkedList[B]()
}

case class NonEmptyLinkedList[A](override val head : A, override val tail : LinkedList[A]) extends LinkedList[A] {
  override def isEmpty: Boolean = false
  override def add(element: A): LinkedList[A] = NonEmptyLinkedList[A](element, this)
  override def map[B](transform: A => B): LinkedList[B] = NonEmptyLinkedList[B](transform(head), tail.map(transform))

  override def filter(predicate: A => Boolean): LinkedList[A] =
    if (predicate(head)) NonEmptyLinkedList(head, tail.filter(predicate))
    else tail.filter(predicate)

  override def ++(anotherList: LinkedList[A]): LinkedList[A] = NonEmptyLinkedList(head, tail ++ anotherList)
  override def flatMap[B](transform: A => LinkedList[B]): LinkedList[B] = transform(head) ++ tail.flatMap(transform)

  override def toString: String = {
    @tailrec
    def stringConcatenationHelper(remaining : LinkedList[A], acc : String) : String =
      if(remaining.isEmpty) acc
      else stringConcatenationHelper(remaining.tail, s"$acc, ${remaining.head}")

    s"[${stringConcatenationHelper(tail, head.toString)}]"
  }
}

object LinkedListTest {
  def main(args: Array[String]): Unit = {

    val first3Numbers : LinkedList[Int] = EmptyLinkedList().add(3).add(2).add(1)
    val someNumbers : LinkedList[Int] = NonEmptyLinkedList(10, NonEmptyLinkedList(20, NonEmptyLinkedList(30, EmptyLinkedList())))
    val someStrings : LinkedList[String] = NonEmptyLinkedList("Hello", NonEmptyLinkedList("I love", NonEmptyLinkedList("Scala", EmptyLinkedList())))
    println(s"first3Numbers = $first3Numbers")
    println(s"someNumbers = $someNumbers")
    println(s"someStrings = $someStrings")

    println(someNumbers.map(x => x + 1))
    println(someStrings.map(x => x.charAt(0) -> x))
    println(someStrings.map(x => NonEmptyLinkedList(x, NonEmptyLinkedList(x + 1, EmptyLinkedList()))))
    println(first3Numbers.filter(_ % 2 != 0))
    println(someStrings.flatMap(x => NonEmptyLinkedList(x, NonEmptyLinkedList(x + 1, EmptyLinkedList()))))

    val stringIntConcatenation = for {  // since we have flatMap and map for comprehension is working here
      number <- first3Numbers
      aString <- someStrings
    } yield s"$number - $aString"

    println(stringIntConcatenation)


  }
}