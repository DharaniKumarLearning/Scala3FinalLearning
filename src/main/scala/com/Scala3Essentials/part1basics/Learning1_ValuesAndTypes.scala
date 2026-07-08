package com.Scala3Essentials.part1basics

object Learning1_ValuesAndTypes {

  def main(args: Array[String]) : Unit = {

    val meaningOfLife: Int = 42 // values are similar to constants in other programming language
    // meaningOfLife = 47 -- We can not reassign to val

    val anInteger = 67 // specifying type while declaring values is optional the compiler is smart enough to infer it from the value we assigned
    // figuring the type of the value by the compiler is called "type inference"

    val aBoolean: Boolean = true
    val aChar: Char = 'A'
    val aString: String = "Hello World!"

    val anInt: Int = 45242 // 4 bytes
    val aShort: Short = 5263  // 2 bytes
    val aLong: Long = 4816541864L  // 8 bytes

    val aFloat : Float = 2.4f  // 4 bytes
    val aDouble : Double = 3.14 // 8 bytes

    // Int, Boolean, Short, Char, String, Float, Double are primitive types in Scala which are mapped to regular types in Java

    println(
      s"""
         |meaningOfLife = $meaningOfLife
         |anInteger = $anInteger
         |aBoolean = $aBoolean
         |aChar = $aChar
         |aString = $aString
         |anInt = $anInt
         |aShort = $aShort
         |aLong = $aLong
         |aFloat = $aFloat
         |aDouble = $aDouble
         |""".stripMargin)

  }

}
