package com.Scala3Essentials.part3fp

import scala.annotation.tailrec

object Learning3_HOFsCurrying {

  /*
    HOF -- Higher Order Functions.
    Functions can take functions as arguments and can return functions as results these functions are called Higher Order Functions
  */

  def main(args: Array[String]): Unit = {

    val aHof : (Int, Int => Int) => Int = (x, func) => func(x + 1)
    val anotherHof : Int => Int => Int = x => y => y + 2 * x
    println(aHof(9, x => x * 2))
    println(anotherHof(8)(2))

    @tailrec
    def nTimes(f : Int => Int, n : Int, x : Int) : Int =
      if(n <= 0) x
      else nTimes(f, n - 1, f(x))

    val tenThousand = nTimes(x => x + 1, 10000, 1)
    println(tenThousand)

    // need some practice to understand this code
    def nTimesV2(f : Int => Int, n : Int) : Int => Int =
      if(n <= 0) (x:Int) => x
      else (x:Int) => nTimesV2(f, n - 1)(f(x))

    val tenThousandV2 = nTimesV2(x => x + 1, 1000)
    println(tenThousandV2(1))

    // currying is basically HOFs returning function instances
    // curried methods = methods with multiple argument list
    def curriedFormatter(fmt : String)(x: Double) : String = fmt.format(x)
    val standardFormat : Double => String = curriedFormatter("%4.2f")
    val preciseFormat : Double => String = curriedFormatter("%10.8f")
    println(standardFormat(Math.PI))
    println(preciseFormat(Math.PI))

    /*
      Exercises
         1. def toCurry(f : (Int,Int) => Int) : Int => Int => Int  -- convert function from non-curried version to curried version
            def fromCurry(f : Int => Int => Int) : (Int, Int) => Int

         2. def compose(f,g) => x => f(g(x))
            def andThen(f,g) => x => g(f(x))
    */

    def toCurry(f : (Int,Int) => Int) : Int => Int => Int = x => y => f(x,y)
    val superAdderV2 = toCurry(_ + _)
    val someAddition = superAdderV2(10)
    println(someAddition(100))
    println(superAdderV2(13131)(414))

    def fromCurry(f : Int => Int => Int) : (Int, Int) => Int = (x, y) => f(x)(y)
    val simpleAdder = fromCurry(superAdderV2)
    println(simpleAdder(10,20))

    def compose(f : Int => Int, g : Int => Int) : Int => Int = x => f(g(x))
    val incrementer = (x: Int) =>  x + 1
    val doubler = (x : Int) =>  x * 2
    val composedApplication = compose(incrementer, doubler)
    println(composedApplication(14))

    def andThen(f : Int => Int, g : Int => Int) = (x: Int) => g(f(x))
    val aSequencedApplication = andThen(incrementer, doubler)
    println(aSequencedApplication(14))

    def composeGeneric[A, B, C](f : B => C, g : A => B) : A => C = x => f(g(x))
    println(composeGeneric(incrementer, doubler)(14))

    def andThenGeneric[A, B, C](f : A => B, g : B => C) : A => C = x => g(f(x))
    println(andThen(incrementer, doubler)(14))


  }
}
