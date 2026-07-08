package com.AdvancedScala.part1as

import scala.annotation.tailrec

object Learning2_AdvancedPatternMatching {

  class Person(val name: String, val age : Int)
  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if(person.age < 18) None
      else Some((person.name, person.age))

    def unapply(age : Int) : Option[String] =
      if(age < 21) Some("Minor")
      else Some("legally allowed to drink")
  }

  def main(args: Array[String]): Unit = {

    val dharani = new Person("Dharani", 10)
    val dharaniPatternMatching = dharani match {
      case Person(n,a) => s"Hi there, I am $n with $a years old"
      case _ => s"The person is not old enough"
    }
    /*
      The above code roughly desugars to
        val result = Person.unapply(dharani)
        result match {
          case Some((n,a)) => ...
          case None => ...

      The variable on which we are calling match gets passed an argument to Person companion object unapply method
      The returned values are stored in case class pattern in match
    */
    println(dharaniPatternMatching)

    val dharaniLegalStatus = dharani.age match {
      case Person(status) => s"Can dharani drink ? $status"
    }
    println(dharaniLegalStatus)

    // Boolean patterns
    object even {
      def unapply(arg: Int): Boolean = arg % 2 == 0
    }

    object SingleDigit {
      def unapply(arg: Int): Boolean = arg > -10 && arg < 10
    }

    val anInt : Int = 2
    val matchProperty = anInt match {
      case even() => "an even number"
      // anInt is passed internally to even object unapply method since unapply is returning Boolean we don't have any variable in case class pattern
      case SingleDigit() => "a single Digit"
      case _ => "no special power"
    }
    println(matchProperty)

    // infix patterns
    infix case class Or[A,B](a : A, b : B)
    val anEither = Or[Int,String](2, "Two")
    val humanDescription = anEither match {
      case number Or string => s"$number is written as $string"
      // equivalent to case Or(number,string) since we have infix we can write like this
    }
    println(humanDescription)

    val aList = List(1,2,3,4)
    val listPatternMatching = aList match {
      case 1 :: rest => s"aList with head 1 and the rest $rest"
      // internally there is a case class :: which takes two arguments we are just calling it in infix notation that's all
      case _ => "non interesting list"
    }
    println(listPatternMatching)

    // decomposing sequences
    val varArg = aList match {
      case List(1,_*) => "List starting with 1"
      case _ => "Some other list"
    }
    println(varArg)

    abstract class MyList[A] {
      def head : A = throw new NoSuchElementException
      def tail : MyList[A] = throw new NoSuchElementException
    }

    case class Empty[A]() extends MyList[A]
    case class Cons[A](override val head : A, override val tail : MyList[A]) extends MyList[A]

    object MyList {
      def unapplySeq[A](list : MyList[A]) : Option[Seq[A]] =
        @tailrec
        def unapplyAuxiliary(remaining : MyList[A], acc : Option[Seq[A]]) : Option[Seq[A]] =
          if(remaining == Empty()) acc
          else unapplyAuxiliary(remaining.tail, Option(acc.getOrElse(Seq.empty) :+ remaining.head))

        unapplyAuxiliary(list.tail, Option(Seq(list.head)))
    }

    val myList : MyList[Int] = Cons(1, Cons(2, Cons(3, Empty())))
    val varArgCustom = myList match {
      case MyList(1, 2, _*) => "my list starting 1"  // here the unapplySeq method will get called
      case _ => "I don't care"
    }
    println(varArgCustom)

    abstract class Wrapper[T] {
      def isEmpty : Boolean
      def get : T
    }

    object PersonWrapper {
      def unapply(person : Person): Wrapper[String] = new Wrapper[String] {
        override def isEmpty: Boolean = false
        override def get: String = person.name
      }
    }

    val weirdPersonPatternMatching = dharani match {
      case PersonWrapper(name) => s"My name is $name"
    }
    println(weirdPersonPatternMatching)

  }
}
