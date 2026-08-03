package com.ClaudeQuestions

import scala.util.Try

object Batch4Questions {
  def main(args: Array[String]): Unit = {

    // Question 31 -- a. Some(10) b. None c. None d. Some(30)

    def safeDivide(a: Int, b: Int): Option[Int] = if(b == 0) None else Some(a/b)
    println(safeDivide(100, 2).flatMap(x => safeDivide(x,5)).flatMap(y => safeDivide(y,0)))

    def parseAndAdd(s1: String, s2: String): Try[Int] = {
        for {
          s1Int <- Try(s1.toInt)
          s2Int <- Try(s2.toInt)
        } yield s1Int + s2Int
    }

    println(parseAndAdd("10", "20"))
    println(parseAndAdd("10", "abc"))

    case class Address(street: Option[String])
    case class Company(address: Option[Address])
    case class Employee(company: Option[Company])

    val emp = Employee(Some(Company(Some(Address(Some("MG Road"))))))
    val emp2 = Employee(Some(Company(None)))
    println(emp.company.flatMap(comp => comp.address).flatMap(address => address.street).getOrElse("Unknown"))
    println(emp2.company.flatMap(comp => comp.address).flatMap(address => address.street).getOrElse("Unknown"))

    sealed trait Result
    case class Success(value: Int) extends Result
    case class Error(message: String) extends Result
    case class Pending(retryAfter: Int) extends Result

    def describe(r: Result): String = r match {
      case Success(value) => s"This is a Success instance with value $value"
      case Error(message) => s"This is an Error instance with message $message"
      case Pending(retryAfter) => s"This is an Pending instance with $retryAfter"
    }

    println(describe(Success(20)))

    // Question 36 -- yes they are same because flatten and flatMap(identity) are same

    def describeMatch(list: List[Int]): String = list match {
      case something if something.isEmpty => "empty"
      case List(value) => s"single: $value"
      case List(first, second) => s"pair: $first $second"
      case List(1,2,rest @ _*) => s"starts with 1,2 and has ${rest.length} more"
      case someList => s"list of size ${someList.length}"
    }

    println(describeMatch(List()))
    println(describeMatch(List(10)))
    println(describeMatch(List(10, 20)))
    println(describeMatch(List(1,2,3,4,5)))
    println(describeMatch(List(10,2,3,4,5,5,2,4,2,1)))

    def classify(x: Int): String = x match {
      case someValue if someValue < 0 => "negative"
      case someValue if someValue == 0 => "zero"
      case someValue if someValue < 10 => "small positive"
      case someValue if someValue < 100 => "medium positive"
      case _ => "large positive"
    }

    println(classify(9))
    println(classify(90))
    println(classify(900))

    def safeHead[A](list: List[A]): Option[A] = if(list.nonEmpty) Some(list.head) else None
    def safeLast[A](list: List[A]): Option[A] = if(list.length >= 1) Some(list.last) else None

    val result = for {
      a <- Option(5) // a value will be 5
      b <- Option(10) // b value will be 10
      c <- if (a + b > 10) Some(a + b) else None // c value will be 15 since the if condition holds truw
      d <- Option(c * 2) // d value will be 30
    } yield d

    println(result) // Some(30)

   }
}
