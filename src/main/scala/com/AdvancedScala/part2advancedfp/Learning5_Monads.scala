package com.AdvancedScala.part2advancedfp

import scala.annotation.targetName

object Learning5_Monads {

  private def listStory() : Unit = {

    val aList = List(1,2,3)
    val listMultiply = for {
      x <- List(1,2,3)
      y <- List(4,5,6)
    } yield x * y

    println(listMultiply)

    // for comprehensions = chains of map + flatMap
    val listMultiply_v2 = List(1,2,3).flatMap(x => List(4,5,6).map(y => x * y))
    println(listMultiply_v2)

    val f = (x : Int) => List(x,x + 1)
    val g = (x : Int) => List(x,2 * x)
    val pure = (x:Int) => List(x)

    val leftIdentity = pure(42).flatMap(f) == f(42)  // for every x and for every f
    val rightIdentity = aList.flatMap(pure) == aList  // for every list

    /*
      [1,2,3].flatMap(x => [x, x + 1] = [1,2,2,3,3,4]
      [1,2,2,3,3,4].flatMap(x => [x, 2 * x]) = [1,2,2,4, 2,4,3,6, 3,6,4,8]

      [1,2,2,4] = f(1).flatMap(g)
      [2,4,3,6] = f(2).flatMap(g)
      [3,6,4,8] = f(3).flatMap(g)

      [1,2,2,4, 2,4,3,6, 3,6,4,8] = f(1).flatMap(g) ++ f(2).flatMap(g) ++ f(3).flatMap(g)
      [1,2,3].flatMap(x => f(x).flatMap(g))
    */

    val associativity = aList.flatMap(f).flatMap(g) == aList.flatMap(x => f(x).flatMap(g))
    println(s"leftIdentity = $leftIdentity")
    println(s"rightIdentity = $rightIdentity")
    println(s"associativity = $associativity")
  }

  private def optionStory() : Unit = {
    val anOption : Option[Int] = Option(42)

    val optionString = for {
      lang <- Option("Scala")
      version <- Option(3)
    } yield s"$lang-$version"
    println(s"optionString = $optionString")

    val optionString_v2 = Option("Scala").flatMap(x => Option(3).map(y => s"$x-$y"))
    println(s"optionString_v2 = $optionString_v2")

    val f = (x:Int) => Option(x + 1)
    val g = (x:Int) => Option(2 * x)
    val pure = (x:Int) => Option(x)

    val leftIdentity = pure(43).flatMap(f) == f(43)
    val rightIdentity = anOption.flatMap(pure) == anOption
    val associativity = anOption.flatMap(f).flatMap(g) == anOption.flatMap(x => f(x).flatMap(g))

    println(s"leftIdentity = $leftIdentity")
    println(s"rightIdentity = $rightIdentity")
    println(s"associativity = $associativity")
  }

  // MONADS - chain dependent computations

  case class PossiblyMonad[A](unsafeRun: () => A) {
    def map[B](f : A => B) : PossiblyMonad[B] = PossiblyMonad[B](() => f(unsafeRun()))
    def flatMap[B](f : A => PossiblyMonad[B]) : PossiblyMonad[B] = PossiblyMonad[B](() => f(unsafeRun()).unsafeRun())
  }

  object PossiblyMonad {
    @targetName("pure")
    def apply[A](value : => A) : PossiblyMonad[A] = new PossiblyMonad[A](() => value)
  }

  // this possiblyMonad is a MONAD
  private def possiblyMonadStory() : Unit = {

    val possiblyMonad = PossiblyMonad(43)

    val f = (x:Int) => PossiblyMonad(x + 1)
    val g = (x:Int) => PossiblyMonad(x * 2)
    val pure = (x:Int) => PossiblyMonad(x)

    val leftIdentity = pure(44).flatMap(f) == f(44)
    val rightIdentity = possiblyMonad.flatMap(pure) == possiblyMonad
    val associativity = possiblyMonad.flatMap(f).flatMap(g) == possiblyMonad.flatMap(x => f(x).flatMap(g))

    println(PossiblyMonad(3) == PossiblyMonad(3))  // false
    println(s"leftIdentity = $leftIdentity")
    println(s"rightIdentity = $rightIdentity")
    println(s"associativity = $associativity")

    // real tests..values produced
    val leftIdentity_v2 = pure(44).flatMap(f).unsafeRun() == f(44).unsafeRun()
    val rightIdentity_v2 = possiblyMonad.flatMap(pure).unsafeRun()  == possiblyMonad.unsafeRun()
    val associativity_v2 = possiblyMonad.flatMap(f).flatMap(g).unsafeRun()  == possiblyMonad.flatMap(x => f(x).flatMap(g)).unsafeRun()
    println(s"leftIdentity_v2 = $leftIdentity_v2 ")
    println(s"rightIdentity_v2  = $rightIdentity_v2 ")
    println(s"associativity_v2  = $associativity_v2 ")

  }

  private def possiblyMonadExample() : Unit = {
    val aPossiblyMonad = PossiblyMonad { // this println won't get executed at the construction
      println("printing my first possible monad")
      42
    }

    val anotherPM = PossiblyMonad {
      println("my second monad")
      "Scala"
    }

    val aForComprehension = for { // computations are described but not executed
      num <- aPossiblyMonad
      lang <- anotherPM
    } yield s"$num-$lang"


  }

  def main(args: Array[String]): Unit = { 
    listStory()
    optionStory()
    possiblyMonadStory()
    possiblyMonadExample()

    // The PossiblyMonad we created in this file is actually IO in scala library
  }
}
