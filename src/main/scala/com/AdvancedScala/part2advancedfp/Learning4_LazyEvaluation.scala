package com.AdvancedScala.part2advancedfp

object Learning4_LazyEvaluation {
  def main(args: Array[String]): Unit = {

    lazy val x : Int = {
      println("Hello")
      42
    }

    // lazy keyword delays the evaluation of a value until the first use
    // evaluation of x occurs only once
    println(x)
    println(x) // this doesn't print Hello because the value of x is already evaluated

    def byNameMethod(n: => Int) : Int = n + n + n + 1
    def retrieveMagicValue() : Int = {
      println("waiting")
      Thread.sleep(1000)
      42
    }

    def demoByName() : Unit = {
      println(byNameMethod(retrieveMagicValue()))
      // retrieveMagicValue() + retrieveMagicValue() + retrieveMagicValue()
    }

    demoByName()

    def byNeedMethod(n: => Int) : Int = {
      lazy val someResult = n  // memoization
      someResult + someResult + someResult + 1
    }

    def demoByNeed() : Unit = println(byNeedMethod(retrieveMagicValue()))
    demoByNeed()

    def lessThan30(i : Int) : Boolean = {
      println(s"$i is less than 30?")
      i < 30
    }

    def greaterThan20(i : Int) : Boolean = {
      println(s"$i is greater than 20?")
      i > 20
    }

    val numbers = List(1, 25, 40, 5, 23)
    // filter creates a new list, then map creates another new list
    def demoFilter() : Unit = {
      val lt30 = numbers.filter(lessThan30)
      val gt20 = lt30.filter(greaterThan20)
      println(gt20)
    }

    demoFilter()

    // withFilter delays filtering — only one collection built at the end 
    def demoWithFilter(): Unit = {
      val lt30 = numbers.withFilter(lessThan30)  // withFilter internally does lazy evaluation
      val gt20 = lt30.withFilter(greaterThan20)
      println(gt20.map(identity))  // identity is basically x => x
    }

    demoWithFilter()

    def demoForComprehension() : Unit = {
      val forComp = for {  // internally uses withFilter
        n <- numbers if lessThan30(n) && greaterThan20(n)
      } yield n
      println(forComp)
    }

    demoForComprehension()

  }
}
