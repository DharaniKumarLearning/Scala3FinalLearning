package com.AdvancedScala.part4contextualabstractions

object Learning2_ExtensionMethods {

  case class Person(name: String) {
    def greet: String = s"Hi My name is $name, nice to meet you"
  }

  extension (str: String) {  // we are extending string type with a new greetAsPerson method
    private def greetAsPerson: String = Person(str).greet  // this is the new method that String type will get from now on
  }

  // generic extension methods
  extension [A](list: List[A]) {
    private def ends: (A, A) = (list.head, list.last)
    private def combineAll(using combinator: Combinator[A]) : A = list.reduce(combinator.combine)
  }

  // The reason generic methods exist is to make APIs very expressive

  trait Combinator[A] {
    def combine(x: A, y: A) : A
  }

  given intCombinator: Combinator[Int] = (x,y) => x * y
  given stringCombinator: Combinator[String] = (x,y) => x + y

  extension (anInt: Int) {
    def isEven : Boolean = anInt % 2 == 0
  }

  def main(args: Array[String]): Unit = {
    val dharaniGreeting = "Dharani".greetAsPerson
    println(dharaniGreeting)
    println(List("A", "B", "C", "Z").ends)
    println(List(10, 20, 30, 40).combineAll)
    println(List("A", "B", "C", "D").combineAll)

    val firstLast = ends(List(10,20,30,40))  // call extension method directly
    println(firstLast)
    println(20.isEven)
  }
}
