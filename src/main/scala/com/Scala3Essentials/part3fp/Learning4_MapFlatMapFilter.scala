package com.Scala3Essentials.part3fp

object Learning4_MapFlatMapFilter {

  def main(args: Array[String]): Unit = {

    val aList = List(1,2,3)
    println(aList.head)  // first element of the list
    println(aList.tail)  // remaining elements of the list

    // map, flatMap and filter
    println(aList.map(x => x + 1))
    println(aList.filter(x => x % 2 != 0))
    val aPair = (x:Int) => List(x, x + 1)
    println(aList.map(x => aPair(x)))
    println(aList.flatMap(x => aPair(x)))

    val numbers = List(1,2,3,4)
    val chars = List('a','b','c','d')
    val colors = List("black", "white", "red")
    val combinations = numbers.flatMap(x => chars.flatMap(y => colors.map(z => s"$x$y - $z")))
    println(combinations)

    // we can achieve the above combinations in a readable fashion using for comprehension

    val combinationsFor = for {  // for is an expression which is compacted version of flatMap
      number <- numbers
      char <- chars
      color <- colors
    } yield s"$number$char - $color"

    println(combinationsFor)

    val combinationsForWithIf = for {
      number <- numbers if number % 2 == 0  // adding the if condition after numbers list reduces some iterations
      char <- chars
      color <- colors  // if condition added here will have more iterations
    } yield s"$number$char - $color"

    println(combinationsForWithIf)

    numbers.foreach(x => println(x))
    for { color <- colors } println(color)

    /*
      Exercises
        1. A small collection of at most one element - MayBe[A]
           add map, flatMap, filter whatever you think is necessary
    */

  }
}
