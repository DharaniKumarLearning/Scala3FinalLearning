package com.Scala3Essentials.part2oop

object Learning4_AccessModifiers {

  class Person(val name: String) {
    def sayHi(): String = s"Hi, my name is $name"
    protected def sayHiProtected() : String = s"Hi, my name is $name"
    private def watchNetflix() : String = "I am binge watching my favorite series..."  // private methods are only accessible inside class
  }

  class Kid(override val name: String, age: Int) extends Person(name) {  // we can override the fields in class constructor directly like this
    def greetPolitely(): String = {  // no modifier for method means the method is public
      sayHiProtected() + " I love to play"  // we can call sayHiProtected because Kid is subclass of parent
    }
  }

  class KidWithParents(override val name: String, age: Int, momName: String, dadName: String) extends Person(name) {
    val mom = new Person(momName)
    val dad = new Person(dadName)
    
    // def everyOneSayHi(): String =
    //  s"Hi, I am $name, and here are my parents " + mom.sayHiProtected() + dad.sayHiProtected()
    // the above is not valid because we are calling sayHiProtected() from different Person objects not with KidWithParents class object
  }

  def main(args: Array[String]) : Unit = {
    val person = new Person("Alice")
    println(person.sayHi())
    // println(person.sayHiProtected()) -- this is not possible because protected method can only be called within class or within its subclasses

    val kid = new Kid("Dharani", 30)
    println(kid.greetPolitely())
  }
}
