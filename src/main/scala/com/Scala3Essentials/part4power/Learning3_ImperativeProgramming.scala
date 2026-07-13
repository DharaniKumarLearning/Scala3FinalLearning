package com.Scala3Essentials.part4power

object Learning3_ImperativeProgramming {
  def main(args: Array[String]): Unit = {

    var aVariable = 99
    println(s"aVariable before changing = $aVariable")

    aVariable = 100  // vars can be reassigned
    println(s"aVariable after changing = $aVariable")
    // aVariable = "Scala" -- we can't assign value of different type

    aVariable += 10
    println(s"aVariable after changing again = $aVariable")

    var i = 0
    while(i < 10) {
      println(s"Counter = $i")
      i += 1
    }

    /*
      Imperative programming (loops/variables/mutable data) are not recommended
        - code becomes hard to read and understand (especially in growing code bases)
        - vulnerable to concurrency problems (e.g. need for synchronization)

      Imperative programming can help
        - for performance critical applications (0.1% of cases: Akka/ZIO/Cats are already quite fast)
        - for interactions with Java libraries (usually mutable)

      Using imperative programming in Scala for no good reason defeats the purpose of Scala
    */

  }
}
