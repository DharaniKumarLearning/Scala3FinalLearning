package com.Scala3Essentials.part1basics

object Learning5_CBNvsCBV {

  // CBV -- CallByValue = arguments are evaluated before function invocation
  private def aFunction(arg: Int) : Int = arg + 1

  // CBN -- CallByName = arguments are passed LITERALLY as an expression, evaluated at every reference
  private def aByNameFunction(arg: => Int): Int = arg + 1

  private def twiceByValue(x: Long): Unit = {
    println(s"By value : $x")
    println(s"By value : $x")
  }

  private def twiceByName(x: => Long) : Unit = {
    println(s"By name : $x")
    println(s"By name : $x")
  }

  private def infinite() : Int = 1 + infinite()
  private def printFirst(x: Int, y: => Int) : Unit = println(x)

  // CallByName arguments are evaluated only when we use them
  
  def main(args: Array[String]) : Unit = {
    println(s"aFunction return value = ${aFunction(10 + 90)}")
    println(s"aByNameFunction return value = ${aByNameFunction(10 + 90)}")
    twiceByValue(System.nanoTime())
    twiceByName(System.nanoTime())
    printFirst(42, infinite())
  }
}
