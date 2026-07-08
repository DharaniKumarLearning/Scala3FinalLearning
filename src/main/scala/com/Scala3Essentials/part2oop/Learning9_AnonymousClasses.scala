package com.Scala3Essentials.part2oop

object Learning9_AnonymousClasses {

  abstract class Animal {
    def eat(): Unit
  }

  class SomeAnimal extends Animal {
    override def eat(): Unit = println("this is the eat method defined in SomeAnimal class")
  }

  class Person(name: String) {
    def sayHi() : Unit = println(s"Hi, My name is $name")
  }

  def main(args: Array[String]): Unit = {

    val someAnimal = new SomeAnimal
    someAnimal.eat()

    // If the SomeAnimal data type is very short-lived, and it is only instantiated like once or twice the whole definition of SomeAnimal class is quite redundant

    // We can instantiate Animal class on the spot which is called AnonymousClass
    // Instantiating an anonymous class is available for abstract classes, traits and non-abstract classes as well
    val someAnimalV2 = new Animal {
      override def eat(): Unit = println("this is the eat method defined in an anonymous class")
    }
    someAnimalV2.eat()

    val jim = new Person("Jim") {
      override def sayHi(): Unit = println("Hi My name is Jim")
    }
    jim.sayHi()

  }
}
