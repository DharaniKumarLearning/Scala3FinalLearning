package com

object Playground {

  println("this is executing before main method")
  def main(args: Array[String]): Unit = {
    println("this is executing in the main method")
  }
  println("this is executing after main method")
}
