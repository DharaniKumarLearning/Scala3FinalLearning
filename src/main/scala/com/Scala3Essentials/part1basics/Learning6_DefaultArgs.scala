package com.Scala3Essentials.part1basics

import scala.annotation.tailrec

object Learning6_DefaultArgs {

  @tailrec
  private def sumUntilTailRec(x: Int, acc: Int = 0): Int =  // default arguments
    if (x < 0) acc
    else sumUntilTailRec(x - 1, acc + x)

  // When you use a function most of the time with the same value = default arguments
  private def savePicture(dirPath: String, name: String, format: String = "jpeg", width: Int = 1920, height: Int = 1080) : Unit =
    println(s"Saving picture in $format in path $dirPath with $name is successful with height $height and width $width")

  def main(args: Array[String]): Unit = {
    val sumUntil100 = sumUntilTailRec(100)  // we can call the function with only one argument
    println(s"sumUntil100 = $sumUntil100")
    savePicture("/Users/dharanikumar/pictures", "my-photo")
    savePicture("/Users/dharanikumar/pictures", "my-photo", "png")  // we can pass different value for default arguments if we want
    savePicture("/Users/dharanikumar/pictures", "my-photo", width = 800, height = 600)  // named arguments are available if we want to pass in different order
  }
}
