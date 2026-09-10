package com.Scala3Essentials.part3fp

object Learning10_Either {
  def main(args: Array[String]): Unit = {

    /*
      Either[A,B] is a type that holds one of two possible values -- either an A or a B, never both
      It has exactly two subtypes.
        Left[A] -- hold a value of type A
        Right[B] -- holds a value of type B
    */

    val a : Either[String, Int] = Right(42)
    val b : Either[String, Int] = Left("something went wrong")

    val result = a match { // extracting value from Either
      case Right(value) => s"Success : $value"
      case Left(error) => s"Failed : $error"
    }

    println(a.getOrElse(0))
    println(b.getOrElse(0)) // if it is left it uses the default value
    println(result)
    println(a.swap)  // Right becomes Left and vice versa
    println(a.map(x => x + 1))  // here the map method will get executed since the value is Right
    println(b.map(x => x + 2))  // the map method won't get executed just like Option None type

    def parseAge(s: String) : Either[String, Int] = {
      if(s.forall(_.isDigit)) Right(s.toInt)
      else Left(s"$s is not a valid number")
    }

    println(parseAge("30"))
    println(parseAge("abc"))

    def divide(a: Int, b: Int) : Either[String, Int] =
      if(b == 0) Left("cannot divide by zero")
      else Right(a / b)

    println(divide(10, 2))
    println(divide(10, 0))

  }
}
