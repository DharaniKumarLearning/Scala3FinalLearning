package com.Scala3Essentials.part1basics

import scala.annotation.tailrec

object Learning3_Functions {

  private def aFunction(a: String, b: Int): String = a + b

  private def aNoArgFunction() : Int = 45
  private def aParameterLessFunction : Int = 56

  // functions can be recursive
  private def stringConcatenation(aString: String, n : Int) : String = {
    if(n <= 1) aString else aString + stringConcatenation(aString, n - 1)
  }

  // When you need loops use recursion

  private def aVoidFunction(aString: String) : Unit = println(aString)
  private def computeDoubleStringWithSideEffect(aString: String) : String = {
    aVoidFunction("Dharani") // side effect
    aString + aString
  }  // these types of functions are generally discouraged in functional programming language

  private def aBigFunction(n : Int) : Int = {
    def aSmallerFunction(a: Int, b: Int) = a + b  // we can define auxiliary functions inside a normal function
    aSmallerFunction(n, n + 1)
  }

  /*
    Exercises
      1. A greeting function (name, age) => "Hi My name is $name, and I am $age years old"
      2. Factorial function
      3. Fibonacci function
      4. A function that tests if a number is prime or not
  */

  private def greeting(name: String, age: Int) : String = s"Hi My name is $name, and I am $age years old"
  private def factorial(n : Int) : Int =
    if(n < 0) 0
    else if(n == 1) 1
    else n * factorial(n - 1)

  private def fibonacci(n : Int) : Int =
    if(n <= 2) 1
    else fibonacci(n - 1) + fibonacci(n - 2)

  private def isPrime(n: Int) : Boolean = {
    @tailrec
    def isPrimeUntil(t: Int) : Boolean =
      if(t <= 1) true
      else n % t != 0 && isPrimeUntil(t - 1)  // && operator is short-circuiting hence isPrimeUntil is tailrec

    isPrimeUntil(n / 2)
  }

  // For recursive functions we always need to specify the return type for normal functions it is optional the compiler can infer it

  def main(args: Array[String]) : Unit = {
    val aFunctionInvocation = aFunction("Dharani", 10)
    println(s"aFunctionInvocation = $aFunctionInvocation")
    println(s"aNoArgFunction return value = ${aNoArgFunction()}")
    println(s"aParameterLessFunction return value = $aParameterLessFunction")
    println(s"stringConcatenation return value = ${stringConcatenation("Scala", 3)}")
    println(s"computeDoubleStringWithSideEffect return value = ${computeDoubleStringWithSideEffect("Scala")}")
    println(s"aBigFunction return value = ${aBigFunction(10)}")
    println(s"greeting return value = ${greeting("Dharani", 30)}")
    println(s"Factorial of 6 is ${factorial(6)}")
    println(s"30th fibonacci number is ${fibonacci(30)}")
    println(s"is 109 a Prime number ? ${isPrime(109)}")
  }
}
