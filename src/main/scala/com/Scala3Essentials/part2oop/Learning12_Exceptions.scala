package com.Scala3Essentials.part2oop

import scala.annotation.tailrec

object Learning12_Exceptions {

  // Exceptions are also classes
  class MyException extends RuntimeException {
    override def getMessage: String = "My Exception"
  }

  def main(args: Array[String]): Unit = {

    val aString : String = null
    // println(aString.length) // java.lang.NullPointerException

    // val aWeirdValue : Int = throw new NullPointerException -- throwing an exception returns Nothing

    // The exception we want to throw must derive from the type Throwable
    // From type Throwable we have type Error like StackOverFlowError, OutOfMemoryError
    // From type Throwable we have type Exception like NullPointerException, NoSuchElementException -- these exceptions we programmers should be aware of


    def getInt(withExceptions : Boolean) : Int =
      if(withExceptions) throw new NullPointerException("No Int for you")
      else 42

    // The type of the variable depends on the consolidated type of both try and catch block
    val potentialFail : Int = try {
      getInt(true)
    } catch {
      // always good practice to specify more specific exceptions at first because the case in catch block are executed sequentially and the parent exception can catch it
      case e : RuntimeException => 54  // matches RunTimeException because it is the parent class of NullPointerException
      case f : NullPointerException => 64
    }

    println(potentialFail)

    val throwingException : Int = try {
      throw new MyException  // throwing our custom exceptions
    } catch {
      case e: MyException => 45
    } finally {
      // finally block will reserve piece of code that will get executed no matter what, and it is optional
      // usually used to closing resources
      // finally has no impact on the return type of try expression
      println("finally")
    }

    println(throwingException)

    def oomCrash() : String = {
      @tailrec
      def bigString(n : Int, acc : String) : String =
        if(n == 0) acc
        else bigString(n - 1, acc + acc)

      bigString(41616364, "Scala")
    }

    // println(oomCrash()) -- this line will cause OOM error because we are creating huge string that doesn't fit in JVM

  }
}
