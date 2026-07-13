package com.Scala3Essentials.part4power

import scala.util.Random

object Learning1_PatternMatching {
  def main(args : Array[String]) : Unit = {

    // Pattern matching is basically switch statement in other programming languages on steroids
    val random = new Random()
    val aValue = random.nextInt(100)

    val description : String = aValue match {
      case 1 => "the first"
      case 2 => "the second"
      case 3 => "the third"
      case _ => s"Something else : $aValue"
    }
    println(description)

    // Pattern matching can be used to decompose values
    case class Person(name : String, age : Int)
    val bob = Person("Bob", 40)

    val greeting = bob match { // this works only on case class not on normal classes
      case Person(n, a) if a < 20 => s"Hi I am a minor with name $n and my age is $a"  // we can add if guards in pattern matching case
      case Person(n, a) => s"Hello there, my name is $n and my age is $a"
      case _ => "I don't know you"
    }
    println(greeting)

    /*
      Patterns are matched in order : put the most specific patterns first
      What if no cases match ? the match expression will throw MatchError
      What's the type returned ? the lowest common ancestor of all types on the right hand side of each branch
    */

    // Pattern matching on sealed hierarchies
    sealed abstract class Animal
    case class Dog(breed : String) extends Animal
    case class Cat(meowStyle : String) extends Animal

    val anAnimal : Animal = Dog("Terra Nova")
    val animalPatternMatching = anAnimal match {
      case Dog(someBreed) => "I've detected a Dog"
      case Cat(style) => "I've detected a Cat"
    }
    println(animalPatternMatching)


  }
}
