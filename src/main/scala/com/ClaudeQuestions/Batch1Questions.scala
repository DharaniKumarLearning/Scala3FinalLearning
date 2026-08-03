package com.ClaudeQuestions

import scala.annotation.tailrec

object Batch1Questions {
  def main(args: Array[String]): Unit = {

    def isEven(n : Int) : Boolean = n % 2 == 0
    println(isEven(2))
    println(isEven(3))

    def sumDigits(n : Int) : Int = {
      @tailrec
      def sumDigitsHelper(n : Int, acc: Int) : Int = {
        if(n == 0) acc
        else sumDigitsHelper(n / 10, acc + (n % 10))
      }
      sumDigitsHelper(n,0)
    }

    println(sumDigits(1234))

    def power(base: Int, exp: Int): BigInt = {
      @tailrec
      def powerHelper(exponent: Int, acc: BigInt) : BigInt = {
        if(exponent == 0) acc
        else powerHelper(exponent - 1, base * acc)
      }
      powerHelper(exp,1)
    }

    println(power(2,10))
    println(power(3,4))

    // Question 4 -- Since the parameter is byName parameter we get evaluated printed twice and the final value 84 gets printed as well

    def applyNTimes(f: Int => Int, n: Int, x: Int): Int = {
      @tailrec
      def applyNTimesHelper(nTimes: Int, acc: Int) : Int = {
        if(nTimes == 0) acc
        else applyNTimesHelper(nTimes - 1, f(acc))
      }
      applyNTimesHelper(n, x)
    }

    println(applyNTimes(x => x + 1, 5, 0))
    println(applyNTimes(x => x + 10, 5, 1))

    def formatter(format: String)(value: Double): String = format.format(value)
    val precisionOf2 = formatter("%.2f")
    val precisionOf5 = formatter("%.5f")
    println(precisionOf2(Math.PI))
    println(precisionOf5(Math.PI))

    def collapseString(s: String): String = {
      @tailrec
      def collapseStringHelper(aString: String, acc : String) : String = {
        if(aString == "") acc
        else collapseStringHelper(aString.tail, if(acc.isEmpty || acc.last.toString != aString.head.toString) acc + aString.head else acc)
      }
      collapseStringHelper(s,"")
    }

    println(collapseString("aabbbcca"))
    println(collapseString("aaaaaabbbbbccccddddaaaadd"))

    // Question 8 -- The value of x is "big" and the type is String
    // Question 9 -- I never understood fibonacci series as it involves two times recursion I can't wrap my head around it hence leave me with that question

    def buildGreeting(name: String, greeting: String = "Hello", punctuation: String = "!"): String = s"$greeting $name$punctuation"
    println(buildGreeting("Dharani"))
    println(buildGreeting("Dharani", "Bye"))
    println(buildGreeting(greeting = "Hi", name = "Kavya"))
    println(buildGreeting("Dharani", "Bye", "!!"))


  }
}
