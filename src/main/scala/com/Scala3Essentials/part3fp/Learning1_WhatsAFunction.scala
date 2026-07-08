package com.Scala3Essentials.part3fp

object Learning1_WhatsAFunction {

  /*

    Functional Programming is a style of writing code in which functions are so called "first-class" citizens
    First class citizens means we operate with functions just like we do with values
    Functional Programming like Scala is running on JVM which is meant for objects but Scala introduced function types to solve this problem

  */

  trait MyFunction[A, B] {
    def apply(arg : A) : B
  }

  def main(args: Array[String]) : Unit = {

    val doubler : MyFunction[Int,Int] = new MyFunction[Int, Int] {
      override def apply(arg: Int): Int = arg * 2
    }

    val meaningOfLife = 42
    val meaningDoubled = doubler.apply(10)
    val meaningDoubledV2 = doubler(20)
    println(meaningDoubled)
    println(meaningDoubledV2)

    val tripleStandard = new Function1[Int, Int] {
      override def apply(v1: Int): Int = v1 * 3
    }

    // all functions are instances of FunctionX with apply methods

    println(tripleStandard(10))

    val adder = new Function2[Int, Int, Int] {
      override def apply(a: Int, b: Int): Int = a + b
    }

    println(adder(100,200))

    /*
      Exercises
        1. A function which takes 2 strings as arguments and concatenates them
        2. Define a function which takes an Int as an argument and returns ANOTHER FUNCTION as result
    */

    val stringConcatenate : (String, String) => String = new Function2[String, String, String] {
      override def apply(v1: String, v2: String): String = v1 + v2
    }

    println(stringConcatenate("Dharani ", "Kumar"))

    val superAdder = new Function1[Int, Function1[Int, Int]] {
      override def apply(x: Int): Int => Int = new Function[Int, Int] {
        override def apply(y: Int): Int = x + y
      }
    }

    val adder2 : Function1[Int,Int] = superAdder(10)
    val anAdditionV2 : Int = adder2(20)
    println(anAdditionV2)
    // this type of passing argument one by one is called currying

    val anAdditionV3 = superAdder(100)(200)
    println(anAdditionV3)

    // function values != methods


  }
}
