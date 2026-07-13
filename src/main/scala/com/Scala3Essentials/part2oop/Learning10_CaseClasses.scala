package com.Scala3Essentials.part2oop

object Learning10_CaseClasses {

  // When we want to store lightweight data structures we can use case classes
  case class Person(name: String, age: Int)

  // case class NoArgs -- this is not possible -- A case class must have at least one parameter list instead we have case object
  private case object UnitedKingdom {
    def name: String = "The UK of GB and NI"
  }

  case class CCWithArgListNoArgs() // legal mainly used in the context of generics

  def main(args: Array[String]): Unit = {

    /*
      Once we add case modifier to class we have automatically activated below properties instantly

          1. class arguments are fields of instance
          2. toString, equals and hashCode methods are automatically implemented
          3. utility methods like copy are available by default
          4. case classes have companion objects automatically generated
          5. case classes are serializable means we can send them over network
          6. case classes have extractor patterns for pattern matching
    */

    val dharani = new Person("Dharani", 30)
    println(s"Name : ${dharani.name}, Age : ${dharani.age}")
    println(dharani)

    val dharaniDuped = new Person("Dharani", 30)
    println(dharani == dharaniDuped)

    val dharaniYounger = dharani.copy(age=25)
    println(dharaniYounger)

    val dharaniV2 = Person("Dharani", 30) // internally calls Person companion object apply method
    // so we don't need new keyword while creating the instance for case class

    println(UnitedKingdom.name)


  }
}
