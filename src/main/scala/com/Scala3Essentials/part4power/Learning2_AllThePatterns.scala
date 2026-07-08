package com.Scala3Essentials.part4power

import scala.util.Random

object Learning2_AllThePatterns {
  def main(args: Array[String]): Unit = {

    object MySingleTon

    val someValue : Any = "Scala"
    val constants = someValue match {
      case 42 => "a number"
      case "Scala" => "THE Scala"
      case true => "the truth"
      case MySingleTon => "a singleton object"
    }
    println(constants)

    val matchAnything = someValue match {
      case _ => "I can match anything at all"
    }
    println(matchAnything)

    val matchAnythingVariable = someValue match {
      case something => s"I've matched anything with $something"
    }
    println(matchAnythingVariable)

    // Tuples Pattern matching
    val aTuple : (Int, Int) = (1,4)
    val tupleMatch = aTuple match {
      case (1, something) => s"A tuple with 1 and $something"
      case (something, 2) => "A tuple with 2 as its second element"
    }
    println(tupleMatch)

    // Pattern matching on tuples can be nested
    val nestedTuple : (Int, (Int, Int)) = (1, (2, 3))
    val matchNestedTuple = nestedTuple match {
      case (_, (2, v)) => "A nested tuple with third element as $3"
    }
    println(matchNestedTuple)

    val anOption : Option[Int] = Option(2)
    val matchOption = anOption match {
      case Some(value) => s"Non-empty value : $value"
      case None => "Empty option"
    }
    println(matchOption)

    // Pattern matching on Lists
    val aStandardList = List(1,2,3,4,42)
    val matchStandardList = aStandardList match {
      case List(1,_,_,_) => "List with 4 elements first is 1"
      case List(2, _*) => "List starting with 2"
      case List(1,2,_, _) :+ 42 => "list ending in 42"
      case head :: tail => s"deconstructed list with head $head and tail $tail"
    }
    println(matchStandardList)

    // Pattern matching using types
    val unknown : Any = 2
    val matchTyped = unknown match {
      case anInt : Int => s"I matched an Int, I can add 2 to it : ${anInt + 2}"
      case aString : String => "I matched a string"
      case _:Double => "I matched a double and I don't care about it"
    }
    println(matchTyped)

    // Pattern matching with name binding for the nested collection
    val someList = List(1,List(2,3))
    val bindingNames = someList match {
      case List(head, rest @ List(_, _)) => s"matched the list with $head and the rest is $rest"
      // we can name the nested collection with a variable name and can use that name in the case
    }
    println(bindingNames)

    // Pattern matching with chained patterns
    val random = new Random()
    val someData : Boolean = random.nextBoolean()
    val multiMatch = someData match {
      case true | false => "I don't care what is the value of the variable"
    }
    println(multiMatch)

    val secondElementSpecial = someList match {
      case List(_, List(secondElement:Int, _)) if secondElement > 5 => "second element inside list is greater than 5"
      case List(_, List(secondElement:Int, _)) if secondElement < 5 => "second element inside list is less than 5"
    }
    println(secondElementSpecial)

    // Exercise
    val aSimpleInt = 45
    val isEven = aSimpleInt match { // this is a bad way of checking whether a number is even or not
      case n if n % 2 == 0 => true
      case _ => false
    }
    val isEvenCorrect = aSimpleInt % 2 == 0
    println(isEven)
    println(isEvenCorrect)

    // Trick Exercise
    val numbers : List[Int] = List(1,2,3,4)
    val numbersMatch = numbers match {
      case listOfStrings : List[String] => "a list of strings"
      case listOfInts : List[Int] => "a list of ints"
    }
    println(numbersMatch)  // this will match List[String] because of java reflection not needed to know just be careful

  }
}
