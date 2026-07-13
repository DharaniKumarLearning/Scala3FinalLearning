package com.AdvancedScala.practice

import scala.annotation.tailrec

abstract class DharaniSet[A] extends (A => Boolean) {
  def contains(elem : A) : Boolean
  def apply(elem : A) : Boolean = contains(elem)
  def +(elem : A) : DharaniSet[A]
  def ++(anotherSet : DharaniSet[A]) : DharaniSet[A]
  def map[B](f : A => B) : DharaniSet[B]
  def flatMap[B](f : A => DharaniSet[B]) : DharaniSet[B]
  def filter(f : A => Boolean) : DharaniSet[A]
  def foreach(f : A => Unit) : Unit
  def -(elem : A) : DharaniSet[A]
  def --(anotherSet : DharaniSet[A]) : DharaniSet[A]
  def &(anotherSet : DharaniSet[A]) : DharaniSet[A]
  def toList : List[A]
}

case class EmptyDharaniSet[A]() extends DharaniSet[A] {
  override def contains(elem: A): Boolean = false
  override def +(elem: A): DharaniSet[A] = NonEmptyDharaniSet(elem, this)
  override def ++(anotherSet: DharaniSet[A]): DharaniSet[A] = anotherSet
  override def map[B](f: A => B): DharaniSet[B] = EmptyDharaniSet[B]()
  override def flatMap[B](f: A => DharaniSet[B]): DharaniSet[B] = EmptyDharaniSet[B]()
  override def filter(f: A => Boolean): DharaniSet[A] = this
  override def foreach(f: A => Unit): Unit = ()
  override def -(elem: A): DharaniSet[A] = this
  override def --(anotherSet: DharaniSet[A]): DharaniSet[A] = this
  override def &(anotherSet: DharaniSet[A]): DharaniSet[A] = this
  override def toList: List[A] = List()
}

case class NonEmptyDharaniSet[A](head : A, tail : DharaniSet[A]) extends DharaniSet[A] {
  override def contains(elem: A): Boolean = elem == head || tail.contains(elem)
  override def +(elem: A): DharaniSet[A] = if(contains(elem)) this else NonEmptyDharaniSet(elem, this)
  override def ++(anotherSet: DharaniSet[A]): DharaniSet[A] = (tail ++ anotherSet) + head
  override def map[B](f: A => B): DharaniSet[B] = tail.map(f) + f(head)
  override def flatMap[B](f: A => DharaniSet[B]): DharaniSet[B] = tail.flatMap(f) ++ f(head)
  override def filter(f: A => Boolean): DharaniSet[A] = {
    if(f(head)) tail.filter(f) + head
    else tail.filter(f)
  }
  override def foreach(f: A => Unit): Unit = {
    @tailrec
    def foreachHelper(dharaniSet: DharaniSet[A]) : Unit = dharaniSet match {
      case EmptyDharaniSet() => ()
      case NonEmptyDharaniSet(hd, tl) => {
        f(hd)
        foreachHelper(tl)
      }
    }
    foreachHelper(this)
  }
  override def -(elem: A): DharaniSet[A] = {
    if(elem == head) tail - elem
    else tail - elem + head
  }
  override def --(anotherSet: DharaniSet[A]): DharaniSet[A] = {
    if(anotherSet.contains(head)) tail -- anotherSet
    else (tail -- anotherSet) + head
  }
  override def &(anotherSet: DharaniSet[A]): DharaniSet[A] = {
    if(anotherSet.contains(head)) (tail & anotherSet) + head
    else tail & anotherSet
  }
  override def toString(): String = {
    @tailrec
    def toStringHelper(dharaniSet: DharaniSet[A], acc : String) : String = dharaniSet match {
      case EmptyDharaniSet() => acc
      case NonEmptyDharaniSet(head, tail) => toStringHelper(tail, if (acc.isEmpty) head.toString else s"$acc, $head")
    }
    toStringHelper(this,"")
  }
  override def toList: List[A] = {
    @tailrec
    def toListHelper(dharaniSet: DharaniSet[A], acc : List[A]) : List[A] = dharaniSet match {
      case EmptyDharaniSet() => acc
      case NonEmptyDharaniSet(head, tail) => toListHelper(tail, head :: acc)
    }
    toListHelper(this, List.empty)
  }
}

object DharaniSet {
  def apply[A](values : A*) : DharaniSet[A] = {
    @tailrec
    def applyHelper(remaining : List[A], acc : DharaniSet[A]) : DharaniSet[A] = {
      if(remaining.isEmpty) acc
      else applyHelper(remaining.tail, acc + remaining.head)
    }
    applyHelper(values.toList, EmptyDharaniSet[A]())
  }
}

object DharaniSetTest {
  def main(args: Array[String]): Unit = {

    // contains, apply, +, ++, map, filter, flatMap, foreach, -, --, &

    val stringSet : DharaniSet[String] = DharaniSet("Dharani", "Kavya", "Mincy", "Shiv")
    println(stringSet)
    println(stringSet.toList)
    println(stringSet.contains("Kavya"))
    println(stringSet.contains("John"))
    println(stringSet("Shiv"))
    println(stringSet + "Rashmi")
    println(stringSet ++ DharaniSet("Ramu", "Somu"))
    println(stringSet.map(x => x.charAt(0) -> x))
    println(stringSet.filter(x => x.startsWith("D") || x.startsWith("K")))
    println(stringSet.flatMap(x => DharaniSet(x, x.substring(0,3))))
    stringSet.foreach(x => println(x))
    println(stringSet - "Shiv")
    println(stringSet -- DharaniSet("Dharani", "Shiv", "Lakshmi"))

    val stringNumbers = for {
      string <- stringSet
      num <- DharaniSet(1,2,3,4)
    } yield s"$string,$num"

    println(stringNumbers)

  }
}
