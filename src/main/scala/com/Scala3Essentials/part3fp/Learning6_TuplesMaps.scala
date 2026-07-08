package com.Scala3Essentials.part3fp

object Learning6_TuplesMaps {
  def main(args: Array[String]): Unit = {

    val aTuple : (Int, String) = (2, "RockTheJVM")
    println(aTuple)
    println(aTuple._1)  // accessing the first field
    println(aTuple.copy(_1 = 3))
    println(2 -> "RockTheJVM")  // this is similar to a two valued tuple

    val phoneBook : Map[String,Int] = Map(
      "Jim" -> 555,
      "Daniel" -> 666,
      "Dharani" -> 123
    ).withDefaultValue(-1)  // If the key we try to access doesn't exist then we can return this default value

    println(phoneBook.contains("Dharani"))
    println(phoneBook("Daniel"))
    println(phoneBook("Mary"))  // without withDefaultValue we will get key not found error

    println(phoneBook + ("Kavya" -> 143))
    println(phoneBook - "Dharani")

    val linearPhoneBook = List(
      "Jim" -> 555,
      "Daniel" -> 666,
      "Dharani" -> 123
    )

    val phoneBookV2 = linearPhoneBook.toMap  // converting List to Map
    println(phoneBookV2)
    println(phoneBookV2.toList)  // converting Map to List

    println(phoneBook.map(pair => pair._1.toLowerCase -> pair._2))
    println(phoneBook.map((k,v) => k.toUpperCase -> v))
    println(phoneBook.view.filterKeys(p => !p.startsWith("J")).toMap)
    println(phoneBook.view.mapValues(number => s"0255-$number").toMap)

    val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Dharani")
    val nameGroupings = names.groupBy(x => x.charAt(0))
    println(nameGroupings)

  }
}
