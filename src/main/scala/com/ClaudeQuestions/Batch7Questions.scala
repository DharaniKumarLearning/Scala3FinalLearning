package com.ClaudeQuestions

object Batch7Questions {

  class Box[A](val value: A) {
    def map[B](f: A => B) : Box[B] = new Box(f(value))
    def flatMap[B](f: A => Box[B]) : Box[B] = f(value)
  }

  sealed trait Validated[A] {
    def map[B](f : A => B) : Validated[B]
    def flatMap[B](f : A => Validated[B]) : Validated[B]
  }

  case class Invalid[A](error: String) extends Validated[A] {
    override def map[B](f: A => B): Validated[B] = Invalid[B](error)
    override def flatMap[B](f: A => Validated[B]): Validated[B] = Invalid[B](error)
  }

  case class Valid[A](value: A) extends Validated[A] {
    override def map[B](f: A => B): Validated[B] = Valid(f(value))
    override def flatMap[B](f: A => Validated[B]): Validated[B] = f(value)
  }

  def validateAge(age: Int): Validated[Int] = {println("validating age");if (age > 18) Valid(age) else Invalid("invalid age")}
  def validateName(name: String): Validated[String] = { println("validating name") ; if (name.nonEmpty) Valid(name) else Invalid("name is empty") }
  def validateEmail(email: String) : Validated[String] = { println("validating email") ; if(email.contains("@")) Valid(email) else Invalid("invalid email id") }

  class Logger[A](val value: A, val log: List[String]) {
    def map[B](f : A => B) : Logger[B] = new Logger[B](f(value), log)
    def flatMap[B](f : A => Logger[B]) : Logger[B] = new Logger[B](f(value).value,log ++ f(value).log)
  }

  def main(args: Array[String]): Unit = {

    val result = for {
      x <- Box(10)
      y <- Box(x + 5)
    } yield x + y

    println(result.value)

    // List(111,211,121,221,112,212,122,222) -- 8 elements

    val result1 = for {
      age <- validateAge(25)
      name <- validateName("")
      email <- validateEmail("dharani@apple.com")
    } yield s"$name, $age, $email"

    println(result1)

    val anOption : Option[String] = Option("Hello")

    val f = (x:String) => Option(x + "f")
    val g = (x:String) => Option(x + "g")
    val pure = x => Option(x)

    println(anOption.flatMap(f) == f("Hello"))
    println(anOption.flatMap(pure) == anOption)
    println(anOption.flatMap(f).flatMap(g) == anOption.flatMap(x => f(x).flatMap(g)))

    // None since if x + y > 10 breaks the chain

    val result2 = for {
      a <- Logger(5, List("started with 5"))
      b <- Logger(a + 3, List("added 3"))
      c <- Logger(b * 2, List("doubled"))
    } yield c

    println(result2.value)
    println(result2.log)

    val result4 = for {
      x <- List(1,2,3)
      y <- List(4,5)
      z <- List(true,false)
    } yield (x,y,z)

    println(result4)

    // No because the flatMap method on Option returns another Option which can not be chained with List flatMap method

    def sequence[A](list: List[Option[A]]): Option[List[A]] = {
      val result = list.tail.foldLeft(if(list.head.isDefined) Option(List(list.head.get)) else None) {
        case (acc,x) if acc.isEmpty => None
        case (acc,x) if acc.nonEmpty && x.isDefined => Option(x.get :: acc.get)
        case (acc,x) if acc.nonEmpty && x.isEmpty => None
      }
      if(result.isDefined) Option(result.get.reverse) else None
    }

    println(sequence(List(Some(1), Some(2), Some(3))))
    println(sequence(List(Some(1), None, Some(3))))

    def sequenceForComprehension[A](list: List[Option[A]]): Option[List[A]] = {
      val result = list.tail.foldLeft(if(list.head.isDefined) Option(List(list.head.get)) else None) { (acc,x) =>
        for {
          list <- acc
          value <- x
        } yield value :: list
      }
      if(result.isDefined) Option(result.get.reverse) else None
    }

    println(sequenceForComprehension(List(Some(1), Some(2), Some(3))))
    println(sequenceForComprehension(List(Some(1), None, Some(3))))


  }
}
