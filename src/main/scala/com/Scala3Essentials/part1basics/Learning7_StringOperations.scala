package com.Scala3Essentials.part1basics

object Learning7_StringOperations {
  def main(args: Array[String]) : Unit = {

    val aString : String = "Hello, I am learning Scala and I'm loving it"  // String index starts with 0
    println(s"The second character of the string is : ${aString.charAt(1)}")  // charAt returns the character in the string at specified index
    println(s"The characters in the string till index 5 is : ${aString.substring(0,5)}")
    println(s"Splitting the string based on space : ${aString.split(" ").toList}")  // split method returns an Array
    println(s"Does the string start with Hello : ${aString.startsWith("Hello")}")
    println(s"Replacing all the spaces with hyphens in the string : ${aString.replace(" ","-")}")
    println(s"Converting the string to uppercase : ${aString.toUpperCase()}")
    println(s"Converting the string to lowercase : ${aString.toLowerCase()}")
    println(s"The length of the string is : ${aString.length}")

    println(s"Reverse of the string is : ${aString.reverse}")
    println(s"The first 4 characters of the string is : ${aString.take(4)}")
    println(s"Converting String to Int : ${"2".toInt + 4}")
  }
}
