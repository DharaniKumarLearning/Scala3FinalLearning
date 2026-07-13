package com.Scala3Essentials.part2oop

import scala.language.postfixOps

object Learning2_MethodNotations {

  private class Person(val name: String, age: Int, favoriteMovie : String) {
    infix def likes(movie : String) : Boolean = movie == favoriteMovie  // infix keyword is introduced in Scala 3
    infix def +(person: Person) : String = s"${this.name} is hanging out with ${person.name}"
    infix def +(nickname: String) : Person = new Person(name + nickname, age, favoriteMovie)
    infix def !!(progLanguage : String) : String =
      s"$name wonders how can $progLanguage can be so cool"

    // prefix position
    // unary operators supported by Scala : -, +, ~, !
    def unary_- : String = s"$name's alter ago"
    def unary_+ : Person = new Person(name, age + 1, favoriteMovie)

    // postfix notation
    def isAlive : Boolean = true

    // special methods
    def apply() : String = s"Hi My name is $name, I am $age years old and I really enjoy watching my favorite movie \"$favoriteMovie\""
    def apply(n : Int) : String = s"$name watched $favoriteMovie $n times"
  }

  /*
    Exercise:
      - a + operator on Person class that returns a nickname
          mary + "the rockstar" => new Person("Mary the rockstar")
      - a unary + operator that increases the person's age
          +mary => new Person(_,_, age + 1)
      - an apply method with an int argument
          apply(2) => "Mary watched Inception 2 times"
  */

  def main(args: Array[String]) : Unit = {

    val mary = new Person("Mary", 34, "Inception")
    val john = new Person("John", 36, "FightClub")

    println(s"Does mary like movie FightClub : ${mary.likes("FightClub")}")
    println(s"Does mary like movie FightClub : ${mary likes "FightClub"}")  // we can call the likes method in infix notation as well
    // infix notation is applicable for methods with only one argument
    println(mary + john)
    println(mary.+(john)) // we can call the + method normally as well
    println(mary !! "Scala")
    println(-mary)
    println(mary.unary_-)  // this is similar to -mary
    println(mary.isAlive)
    println(mary isAlive)  // we need to import scala.language.postfixOps for this to work
    // postfix notation is very highly discouraged it is better to stick with dot notation
    println(mary.apply())
    println(mary())  // this is same as mary.apply()

    // Calling exercise methods
    println((mary + " the rockstar")())
    println((+mary)())
    println(mary(10))

  }
}
