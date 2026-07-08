package com.AdvancedScala.part2advancedfp

object Learning2_FunctionalCollections {
  def main(args: Array[String]): Unit = {

    // Sets are functions which takes A => Boolean
    val aSet : Set[String] = Set("I", "love", "Scala")
    val setContainsScala = aSet.contains("Scala")
    println(s"setContainsScala = $setContainsScala")
    println(s"setContainsScala functional style = ${aSet("Scala")}")
    // In the above line the apply method on Set type is called which will tell whether the elements exist in the Set or not

    // Seqs are functions which will take Int => type of the element inside Seq
    val aSeq : Seq[Int] = Seq(1,2,3,4)
    val anElement = aSeq(2)  // returns element at index 2
    // val nonExistingElement = aSeq(100) -- java.lang.IndexOutOfBoundsException
    // Seqs are not completely functions because for certain values we might get exceptions
    // Seqs are actually PartialFunction[Int, A]

    // Maps[K,V] are functions K => V
    val phoneBook : Map[String, Int] = Map(
      "Alice" -> 12345, "Bob" -> 4618
    )
    val alicePhoneBook = phoneBook("Alice")
    println(s"alicePhoneBook = $alicePhoneBook")
    // println(phoneBook("Dharani")) -- java.util.NoSuchElementException
    // Again like List the Map function doesn't work for all the values it works for finite set of values
    // So Maps actually extend PartialFunction[K,V]



  }
}
