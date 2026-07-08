package com.Scala3Essentials.part2oop

object Learning7_AbstractDataTypes {

  // when we make a class abstract it means the class can have fields and methods without implementation
  abstract class Animal {
    val creatureType : String  // this field is abstract
    def eat() : Unit
    def preferredMeal : String = "anything"  // non-abstract fields/methods are allowed in abstract classes
    // methods with no arguments are called accessor methods
  }

  // if we extend from abstract class then we need to make sure that we implement all abstract fields/methods defined in the class we extended
  class Dog extends Animal {
    override val creatureType: String = "Domestic"
    override def eat(): Unit = println("this is the eat method defined in Dog class")
    override val preferredMeal : String = "Bones"  // we can override an accessor method(without an argument) with a field(val)
    override def toString: String = "dog"
  }

  // traits -- describe behavior
  trait Carnivore {  // Scala3 traits can have constructor arguments
    def eat(animal: Animal) : Unit
  }

  class TRex extends Carnivore {
    override def eat(animal: Animal): Unit = println("I'm a TRex, I eat animals")
  }

  trait ColdBlooded
  class Crocodile extends Animal with Carnivore with ColdBlooded {
    override val creatureType: String = "Wild"
    override def eat(): Unit = println("this is the eat method defined in Crocodile class")
    override def eat(animal: Animal): Unit = println(s"I'm a Crocodile, I am eating $animal")
  }

  def main(args: Array[String]): Unit = {

    val dog = new Dog
    println(dog.creatureType)
    dog.eat()

    val crocodile = new Crocodile
    crocodile.eat(dog)

    // val animal = new Animal -- abstract classes can not be instantiated

    /*
      Practical difference b/w traits and abstract classes
        Scala allows only one class inheritance but Scala allows inheritance from multiple traits
      Philosophical difference is a class is supposed to be a thing whereas trait is a description of a behavior
    */

    /*
      Any
        AnyRef
          All classes we write extend from AnyRef
            scala.Null (the null reference) -- null extends every single class
        AnyVal
          Int, Boolean, Char ...

      scala.Nothing is a proper reference for Any type
    */

    val aNonExistentAnimal : Animal = null
    // val anInt: Int = throw new NullPointerException


  }

}
