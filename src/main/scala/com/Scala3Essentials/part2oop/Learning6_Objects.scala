package com.Scala3Essentials.part2oop

object Learning6_Objects {

  object MySingleTon  { // when we write this line we defined the class and the only instance of that class
    val aField : Int = 45
    def aMethod(x: Int) : Int = x + 1
  }

  class Person(val name: String) {  // methods and fields in classes are used for instance-dependent functionality
    def sayHi() : String = s"Hi, my name is $name"
    override def equals(obj: Any): Boolean = this.name == obj.asInstanceOf[Person].name
  }

  // companions - when the class and object have same name in the same file they are called companions
  object Person {  // methods and fields in objects are used for instance-independent functionality
    // can access each others private fields and methods
    val N_EYES = 2
    def canFly: Boolean = false
  }

  // objects can extend classes
  object BigFoot extends Person("BigFoot")

  // Scala application = object + main
  def main(args: Array[String]): Unit = {

    val theSingleTon = MySingleTon
    val anotherSingleton = MySingleTon
    println(theSingleTon == anotherSingleton)
    println(MySingleTon.aField)  // accessing fields of objects
    println(MySingleTon.aMethod(10))  // accessing method of objects

    val mary = new Person("Mary")
    println(mary.sayHi())
    println(Person.N_EYES)
    println(Person.canFly)

    // equality of objects
    val maryV2 = new Person("Mary")
    println(mary eq maryV2)  // false -- since both are different instances in memory
    println(mary equals maryV2)  // same as eq because the default implementation of equals method checks reference
    println(mary == maryV2)  // this is same as equals
    // If we override the equals method then the output will differ based on our logic

    println(MySingleTon eq MySingleTon)  // this is true because it is single instance


  }

}
