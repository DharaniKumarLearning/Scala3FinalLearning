package com.Scala3Essentials.part2oop

object Learning3_Inheritance {

  class Animal {
    val creatureType : String = "Wild"
    def eat() : Unit = println("this is the eat method defined in Animal class")
  }

  private class Cat extends Animal {
    def crunch() : Unit = {
      eat()  // we can call the Parent class method like this in Child class
      println("this is the crunch method defined in Cat class")
    }

    override def toString: String = "a cat"
  }

  private class Person(val name: String, age: Int)
  private class Adult(name: String, age: Int, idCard: String) extends Person(name, age)
  // we must specify parent class constructor arguments while extending the class

  // overriding
  private class Dog extends Animal {
    override  val creatureType: String = "Domestic"
    override def eat(): Unit = {
      println("this is the eat method defined in Dog class")
    }
    override def toString: String = "a dog"
  }

  // overloading vs overriding
  private class Crocodile extends Animal {
    override val creatureType: String = "Very Wild"
    override def eat(): Unit = println("this is the eat method defined in crocodile class")

    // overloading : multiple methods with same name, different signatures
    // What is a different signature : different argument list (different number of args + different type of args)
    def eat(animal: Animal): Unit = println(s"I am eating $animal")
    
    // the below methods are valid overloads for eat method 
    def eat(person: Person, dog: Dog) : Unit = println("First Person then Dog")
    def eat(dog: Dog, person: Person) : Unit = println("First Dog then Person")
  }

  def main(args: Array[String]): Unit = {

    val cat = new Cat
    println(cat.creatureType) // this is possible because Cat class extends Animal class
    cat.eat()
    cat.crunch()

    val dog = new Dog
    println(dog.creatureType)
    dog.eat()
    println(dog)  // here toString method will get called

    // subtype polymorphism
    val anotherDog : Animal = new Dog
    anotherDog.eat()  // the most specific method that is the Dog class eat method will be called

    val person : Person = new Person("Dharani", 30)

    val crocodile = new Crocodile
    crocodile.eat()
    crocodile.eat(cat)
    crocodile.eat(anotherDog)
    crocodile.eat(dog, person)
    crocodile.eat(person, dog)

  }
}
