package com.Scala3Essentials.part2oop

object Learning1_OOPBasics {

  private class Person(name: String, age: Int)  // When we declare class like this the fields are called class parameters (constructor parameters)

  private class OneMorePerson(val name: String, val age: Int)  { // To make the class parameters as fields we need to use val
    val allCaps : String = name.toUpperCase // this is a field for every instance of OneMorePerson
    def greet(name: String) : Unit = println(s"${this.name} says: Hi $name")  // this always points to current object
    // to disambiguate between method arguments
    def greet() : String = s"Hi Everyone, My name is $name and I am $age years old" // Method overloading
    def this(name: String) = this(name,0)  // the implementation of auxiliary should be the call to primary constructor
    // auxiliary constructors are very rarely used in Scala because of these limitation
    // we can use default arguments in scala to avoid the usage of auxiliary constructors
  }

  /*
    Exercise 1: imagine we are creating a backend for a book publishing house
    Create a Novel and Writer class
    Writer : first name, surname and year
      - method full name
    Novel : name, year of release, author
      - authorAge
      - isWrittenBy(author)
      - copy(new year of release) = new instance of novel
  */

  private class Writer(val firstName: String, val surname: String, val dob: Int) {
    def fullName() : String = s"$firstName $surname"
  }

  private class Novel(val name: String, val yearOfRelease : Int, author : Writer) {
    def authorAge() : Int = yearOfRelease - author.dob
    def isWrittenBy(author: Writer) : Boolean = author == this.author
    def copy(newYearOfRelease : Int) : Novel = new Novel(name, newYearOfRelease, author)
  }

  def main(args: Array[String]): Unit = {

    val aPerson : Person = new Person("John", 26)
    // println(aPerson.name) -- class parameters (constructor parameters) are not accessible outside the class
    val oneMorePerson : OneMorePerson = OneMorePerson("Dharani", 30)
    println(s"name of the person is : ${oneMorePerson.name}")  // we can access instance variables using dot notation
    println(s"age of the person is : ${oneMorePerson.age}")
    println(s"name of the person in uppercase : ${oneMorePerson.allCaps}")
    oneMorePerson.greet("JohnDoe")
    println(oneMorePerson.greet())
    val anotherPerson : OneMorePerson = OneMorePerson("Mincy")  // this is possible because we had auxiliary constructor defined in our class
    println(anotherPerson.greet())

    val writer = new Writer("Dharani Kumar", "Gopavaram", 1995)
    val writerImposter = new Writer("Dharani Kumar", "Gopavaram", 2021)
    val novel = new Novel("Life is Back", 2026, writer)
    println(s"Author's full name : ${writer.fullName()}")
    println(s"Author's age : ${novel.authorAge()}")
    println(s"Is Novel written by Dharani? ${novel.isWrittenBy(writer)}")
    println(s"Is Novel written by DharaniImposter? ${novel.isWrittenBy(writerImposter)}")
    println(s"Copy of the Novel? ${novel.copy(2027).authorAge()}")
  }

}
