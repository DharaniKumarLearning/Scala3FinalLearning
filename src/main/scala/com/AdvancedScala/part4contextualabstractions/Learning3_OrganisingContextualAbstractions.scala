package com.AdvancedScala.part4contextualabstractions

object Learning3_OrganisingContextualAbstractions {

  val aList : List[Int] = List(1,2,3,4)
  private val anOrderedList : List[Int] = aList.sorted

  /*
    Where does the compiler actually find the given Ordering[Int]
      1. First place the compiler checks is local scope
      2. Second place compiler looks for is imported scope
      3. Third place is companion objects of all types involved in method signatures

    The same principles apply to extension methods as well
  */

  given reverseOrdering: Ordering[Int] = (x,y) => y - x
  case class Person(name: String)
  private val persons : List[Person] = List(Person("Dharani"), Person("Mincy"), Person("Kavya"))

  object PersonGivens {
    given personOrdering : Ordering[Person] = (x,y) => y.name.compareTo(x.name)
    extension (p: Person) {
      def greet: String = s"Heya nice to meet you, my name is ${p.name} I'm glad to meet you"
    }
  }

  // import PersonGivens.personOrdering -- importing explicitly
  // import PersonGivens.given Ordering[Person]  -- import a given for a particular type
  // import PersonGivens.given -- import all the givens in the specified package
  // import PersonGivens.* -- Warning * does not import given instances

  object Person {
    given personOrdering : Ordering[Person] = (x,y) => x.name.compareTo(y.name)
    extension (p: Person) {
      def greet: String = s"Hi, my name is ${p.name}, nice to meet you"
    }
  }

  private val sortedPersons : List[Person] = persons.sorted

  def main(args: Array[String]): Unit = {
    println(anOrderedList)
    println(sortedPersons)
    import PersonGivens.*  // * will include extension methods in our scope
    println(persons.head.greet)
  }
}
