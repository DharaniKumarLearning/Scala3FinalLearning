package com.ClaudeQuestions

object Batch5Questions {

  object Even {
    def unapply(x : Int) : Option[Int] = if(x % 2 == 0) Some(x / 2) else None
  }

  object Words {
    def unapplySeq(aString: String) : Option[List[String]] =
      if(aString.isEmpty) None
      else Some(aString.split(" ").toList)
  }

  def main(args: Array[String]): Unit = {

    val divider: PartialFunction[Int, Int] = {
      case x if x > 0 => 100 / x
    }

    println(divider(10))
    val dividerLift = divider.lift
    println(dividerLift(0))
    println(dividerLift(10))

    val small: PartialFunction[Int, String] = {
      case x if x > 0 && x <= 5 => "small"
    }

    val big: PartialFunction[Int, String] = {
      case x if x > 5 && x <= 10 => "big"
    }

    val result = List(1, 5, 7, 10, 15).collect {
      case x => small.orElse(big).lift(x)
    }
    println(result)

    val data = 8
    val finalResult = data match {
      case Even(half) => s"$data is even, half is $half"
    }
    println(finalResult)

    // Question 44 --
    // since collect is filter + map for the elements true and 5.0 there are no matching case hence they will get filtered
    // first 3 elements will satisfy the case statements and returns List(2,5,6)

    "hello world foo" match {
      case Words(a, b, c) => println(s"$a, $b, $c")
    }

    "hello world foo dharani" match {
      case Words(a, b, c, d) => println(s"$a, $b, $c, $d")
    }

    // with map, we need to use filter to get the elements that are defined
    println(List(Some(1), None, Some(3), None, Some(5)).filter(x => x.isDefined).map(x => x.get))

    // whereas with collect if the element doesn't satisfy the case statement then it will get automatically filtered out
    println(List(Some(1), None, Some(3), None, Some(5)).collect {
      case x if x.isDefined => x.get
    })

    def checkAndAdd[A](element: A, key: String, existing: Map[String, List[Any]]) : Map[String, List[Any]] = {
      if(existing.contains(key)) existing + (key -> (element :: existing(key))) else existing + (key -> List(element))
    }

    def groupingElements(list : List[Any]) : Map[String, List[Any]] = {
      list.reverse.foldLeft(Map[String,List[Any]]()) { (acc,x) => x match
        case i : Int => checkAndAdd(x, "Int", acc)
        case i : String => checkAndAdd(x, "String", acc)
        case i : Boolean => checkAndAdd(x, "Boolean", acc)
      }
    }

    println(groupingElements(List(1,2,3, true, true, false, "Dharani", true, 4)))

    infix case class |>[A, B](left: A, right: B)
    val someInstance = |>("One", 1)
    someInstance match {
      case left |> right => println(s"The elements inside case classes are $left and $right")
    }

    val calculator: PartialFunction[(String, Int, Int), Int] = {
      case ("+", a, b) => a + b
      case ("-", a, b) => a - b
      case ("*", a, b) => a * b
    }

    println(calculator.isDefinedAt(("/", 1, 2)))
    println(calculator("+", 10, 20))

    // Question 50 -- since collect is filter + map for the first 2 elements since there is no case they will get filtered
    // for the remaining 3 elements we get List(2,4,6) as the output

  }
}
