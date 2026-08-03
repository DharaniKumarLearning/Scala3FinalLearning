package com.ClaudeQuestions

import scala.annotation.tailrec
import scala.util.{Random, Try}

object Batch6Questions {
  def main(args: Array[String]): Unit = {

    def compose[A](f: A => A, g: A => A): A => A = x => f(g(x))
    println(compose[Int](_ + 1, _ * 2)(3))

    // Question 52 -- before x, computing x along with 10 in the next line, only 10 for the last line since the value of x is already computed

    def byNameVsLazy(n: => Int): Int = n + n + n
    def byNameToLazy(n: => Int): Int = {
      lazy val x = n // I remember this from the course it is called memoization
      x + x + x
    }

    println(byNameVsLazy { println("eval"); 5 })  // eval gets printed thrice
    println(byNameToLazy { println("eval"); 5 })  // eval gets printed only once

    def timed[A](block: => A): (A, Long) = {
      val beforeTime = System.currentTimeMillis()
      val result = block
      (result, System.currentTimeMillis() - beforeTime)
    }

    println(timed({
      Thread.sleep(1000)
      42
    }))

    def unfold[A, S](init: S)(f: S => Option[(A, S)]): List[A] = {
      @tailrec
      def unfoldHelper(start: S, acc: List[A]) : List[A] = {
        f(start) match {
          case Some(value1,value2) => unfoldHelper(value2, value1 :: acc)
          case None => acc
        }
      }
      unfoldHelper(init,List[A]()).reverse
    }

    println(unfold(1)(state => if (state <= 5) Some((state, state + 1)) else None))

    // filter will print all checking first and then for the elements that satisfied the predicate we will get transforming printed also filter creates intermediate collection
    // withFilter won't generate intermediate collection it will print checking and transforming print statements directly for a element that satisfies the predicate

    val aList = (1 to 10).toList
    println(aList.foldLeft(0)((acc,x) => acc + x))
    println(aList.reduce(_ + _))
    println(aList.scan(0)(_ + _)) // scan is creating a new List with all the intermediate value I just learnt it after running the code

    // List(2,4,6,8,10)

    @tailrec
    def retry[A](n: Int)(block: => A): Option[A] = {
      val blockResult = Try(block)
      if(n > 0 && blockResult.isSuccess) Some(blockResult.get)
      else if(n > 0 && blockResult.isFailure) retry(n - 1)(block)
      else None
    }

    println(retry(5)({
      val random = new Random()
      val randomNumber = random.nextInt(100)
      if(randomNumber > 10) throw new RuntimeException("Exception for you")
      else randomNumber
    }))




  }
}
