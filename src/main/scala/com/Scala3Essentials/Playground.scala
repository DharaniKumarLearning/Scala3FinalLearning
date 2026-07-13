package com.Scala3Essentials

import scala.annotation.tailrec

abstract class MyList[T] {
  def head: T = throw new NoSuchElementException("head on empty list")
  def tail: MyList[T] = throw new NoSuchElementException("tail on empty list")
}

case class EmptyList[T]() extends MyList[T]
case class NonEmptyList[T](override val head: T, override val tail: MyList[T]) extends MyList[T]

object MyList {
  def unapplySeq[T](myList: MyList[T]) : Option[Seq[T]] = {
    @tailrec
    def unapplySeqHelper(ml: MyList[T], acc: Option[Seq[T]]) : Option[Seq[T]] = {
      if(ml == EmptyList[T]()) Option(acc.getOrElse(Seq.empty).reverse)
      else unapplySeqHelper(ml.tail, Option(ml.head +: acc.getOrElse(Seq.empty)))
    }
    unapplySeqHelper(myList, Option.empty)
  }
}

object Playground {
  def main(args: Array[String]): Unit = {

    val myListInt : MyList[Int] = NonEmptyList(1, NonEmptyList(2, NonEmptyList(3, EmptyList())))
    println(MyList.unapplySeq(myListInt))

    val myListPatternMatching = myListInt match {
      case MyList(1,2,_*) => s"the first 2 elements in myList are 1 and 2"
    }
    println(myListPatternMatching)
  }
}
