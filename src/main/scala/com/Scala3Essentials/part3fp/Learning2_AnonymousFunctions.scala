package com.Scala3Essentials.part3fp

object Learning2_AnonymousFunctions {
  def main(args: Array[String]): Unit = {

    val doubler : Int => Int = new Function1[Int, Int] {
      override def apply(x: Int): Int = x * 2
    }

    println(doubler(30))

    // There is a shorthand version for creating the above function type
    val doublerV2 : Int => Int = (x : Int) => x * 2
    val adder : (Int, Int) => Int = (x:Int, y: Int) => x + y
    println(doublerV2(30))
    println(adder(1000,2000))

    // The above type of functions are called lambdas = anonymous function instances
    val justDoSomething : () => Int = () => 42
    println(justDoSomething)
    println(justDoSomething())

    // alternate syntax with curly braces
    val stringToInt : String => Int = { (str: String) =>
      println("This lambda converts String to Int")
      str.toInt
    }

    println(stringToInt("30") + 40)

    // type inference
    val doublerV3 : Int => Int = x => x * 2  // type inferred by compiler
    val adderV3 : (Int, Int) => Int = (x, y) => x + y  // even multiple arguments can be inferred by compiler
    println(doublerV3(4739))
    println(adderV3(4717,31719))

    // shortest lambdas
    val doublerV4 : Int => Int = _ * 2  // x => x * 2
    val adderV4 : (Int,Int) => Int = _ + _  // when the argument is used only once
    println(doublerV4(4730))
    println(adderV4(4717,31710))

    val superSpecialAdder : Int => Int => Int = x => y => x + y
    val adderV5 : Int => Int = superSpecialAdder(313)
    println(adderV5(4141))
    println(superSpecialAdder(4183)(31731))

  }
}
