package com.Scala3Essentials.part1basics

object Learning2_Expressions {
  def main(args: Array[String]): Unit = {

    // Expressions are structures that can be evaluated to values
    val meaningOfLife = 40 + 2
    println(s"meaningOfLife = $meaningOfLife")

    // mathematical expressions : +, -, *, /, |, &, <<, >>, ~, ^, >>>, <<<
    val mathExpression = 2 + 3 * 4
    println(s"mathExpression = $mathExpression")

    // comparison expressions : <, <=, >, >=, ==, !=
    val equalityTest = 1 == 2
    println(s"equalityTest = $equalityTest")

    // boolean expressions : !, ||, &&
    val nonEqualityTest = !equalityTest
    println(s"nonEqualityTest = $nonEqualityTest")

    // instructions vs expressions.
    // expressions are evaluated, instructions are executed
    // In Scala and in Functional programming in general we think of expressions
    // Pretty much everything we write in Scala is an expression

    val aCondition = true
    val anIfExpression = if(aCondition) 56 else 67
    println(s"anIfExpression = $anIfExpression")

    // code blocks are also expressions
    // The value of the code block is the value of the last expression that is evaluated
    val aCodeBlock = {
      val localValue = 78
      localValue + 54
    }
    println(s"aCodeBlock = $aCodeBlock")

    val someValue = {
      2 < 3
    }
    println(s"someValue = $someValue")

    val someOtherValue = {
      if(someValue) 239 else 986
      42
    }
    println(s"someOtherValue = $someOtherValue")

    // Unit type is mapped to void type in Java
    val yetAnotherValue : Unit = println("Scala")
    println(s"yetAnotherValue = $yetAnotherValue")
  }
}
