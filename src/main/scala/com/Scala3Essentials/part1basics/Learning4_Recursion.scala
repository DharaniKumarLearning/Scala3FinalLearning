package com.Scala3Essentials.part1basics

import scala.annotation.tailrec

object Learning4_Recursion {

  private def sumUntil(n : Int) : Int =
    if(n <= 0) 0
    else n + sumUntil(n - 1)

  private def sumUntilV2(n: BigInt) : BigInt = {
    @tailrec
    def sumUntilTailRec(x: BigInt, acc: BigInt) : BigInt =
      if(x < 0) acc
      else sumUntilTailRec(x - 1, acc + x)

    sumUntilTailRec(n, 0)
  }

  private def sumNumbersBetween(a: Int, b: Int) : Int = {
    @tailrec
    def sumNumbersBetweenTailRec(n1:Int, n2: Int, acc: Int) : Int = {
      if(n1 > n2) acc
      else sumNumbersBetweenTailRec(n1 + 1,n2, acc + n1)
    }
    sumNumbersBetweenTailRec(a,b,0)
  }

  /*
    Exercises:
      1. Concatenate a string n times using tail recursion
      2. Reverse a string using tail recursion
  */

  private def stringConcatenation(aString: String, n : Int): String = {
    @tailrec
    def stringConcatenationTailRec(counter: Int, acc: String) : String = {
      if(counter == 0) acc
      else stringConcatenationTailRec(counter - 1,acc + aString)
    }
    stringConcatenationTailRec(n, "")
  }

  private def stringReverse(aString : String) : String = {
    @tailrec
    def stringReverseTailRec(n : Int, acc: String) : String = {
      if(n < 0) acc
      else stringReverseTailRec(n - 1, acc + aString(n))
    }
    stringReverseTailRec(aString.length - 1, "")
  }

  def main(args: Array[String]): Unit = {
    println(s"sumUntil return value = ${sumUntil(10)}")
    // println(s"sumUntil return value = ${sumUntil(999999999)}")
    // if we pass very large number to sumUntil it will throw StackOverFlowError

    // To overcome StackOverFlowError we need to use TailRecursion which will reuse the stack
    // In Tail Recursion -- recursive call occurs LAST in its code path
    println(s"sumUntilV2 return value = ${sumUntilV2(10000000)}")
    println(s"sumNumbersBetween return value = ${sumNumbersBetween(17,30)}")
    println(s"stringConcatenation return value = ${stringConcatenation("Dharani", 5)}")
    println(s"stringReverse return value = ${stringReverse("Dharani")}")
  }
}
