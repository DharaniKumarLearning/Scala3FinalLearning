package com.Scala3Essentials.part2oop

object Learning5_PreventingInheritance {

  class Person(name: String) {
    final def enjoyLife() : Int = 42  // If we want to prevent overriding of a method we need to use final keyword
  }

  class Adult(name: String) extends Person(name) {
    // override def enjoyLife(): Int = 999  -- can not override as enjoyLife is final
  }

  final class Animal  // final classes can not be extended
  // class Cat extends Animal

  sealed class Guitars(nStrings: Int)  // when we make the class sealed inheritance is only permitted inside the file
  class ElectricGuitar(nStrings: Int) extends Guitars(nStrings)
  class AcousticGuitar extends Guitars(6)

  // In Scala3 no modifier for a class means not encouraging for inheritance
  // Scala classes should be extensively marked open for inheritance not mandatory, but it is good practice
  open class ExtensibleGuitar(nStrings: Int)
  class DharaniGuitar(nStrings: Int) extends ExtensibleGuitar(nStrings)

  def main(args: Array[String]) : Unit = {
    val adult = new Adult("Dharani")
    println(adult.enjoyLife())  // we can access final members outside the class
  }
}
