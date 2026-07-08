package com.Scala3Essentials.part2oop

object Learning8_Generics {

  // generics helps us to reuse the code on different types
  abstract class MyList[A] { // adding generic type to a class
    def head: A
    def tail: MyList[A]
  }

  class Empty[A] extends MyList[A] {
    override def head: A = throw new NoSuchElementException
    override def tail: MyList[A] = throw new NoSuchElementException
  }

  class NonEmpty[A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  // In Scala, we can have multiple generic types
  trait MyMap[Key, Value]

  object MyList {
    // generic methods
    def from2Elements[A](elem1: A, elem2: A) : MyList[A] = new NonEmpty[A](elem1, new NonEmpty[A](elem2, new Empty[A]))
  }

  def main(args: Array[String]): Unit = {

    val listOfIntegers: MyList[Int] = new NonEmpty[Int](1, new NonEmpty[Int](2, new Empty[Int]))
    val listOfStrings: MyList[String] = new NonEmpty[String]("Scala", new NonEmpty[String]("Language", new Empty[String]))

    println(listOfIntegers.head)
    println(listOfIntegers.tail.head)
    println(listOfStrings.head)
    println(listOfStrings.tail.head)

    val first2Numbers = MyList.from2Elements[Int](1,2)
    val first2NumbersV2 = MyList.from2Elements(1,2)  // we don't need to specify type parameter while calling the function the compiler can infer it
    println(first2Numbers.tail.head)
    println(first2NumbersV2.tail.head)

    // we don't need to pass type while creating the instances for our own classes as well
    val first4Numbers : MyList[Int] = new NonEmpty(1, new NonEmpty(2, new NonEmpty(3, new NonEmpty(4, new Empty))))
    println(first4Numbers.tail.tail.head)

  }

}
