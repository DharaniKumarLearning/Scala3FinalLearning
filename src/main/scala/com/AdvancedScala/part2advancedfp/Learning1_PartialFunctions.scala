package com.AdvancedScala.part2advancedfp

object Learning1_PartialFunctions {
  def main(args: Array[String]): Unit = {

    val aFunction : Int => Int = (x:Int) => x + 1
    println(aFunction(10))

    val aFussyFunction : Int => Int = (x:Int) => {
      if(x == 1) 42
      else if(x == 2) 56
      else if(x == 5) 999
      else throw new RuntimeException("no suitable cases possible")
    }
    println(aFussyFunction(2))

    val aFussyFunction_v2 : Int => Int = (x:Int) => x match {
      case 1 => 42
      case 2 => 56
      case 5 => 999
    }
    println(aFussyFunction_v2(5))

    // partial function
    val aPartialFunction : PartialFunction[Int, Int] = { // this is very similar to x => x match
      case 1 => 42
      case 2 => 56
      case 5 => 999
    }
    println(aPartialFunction(1))
    // println(aPartialFunction(33)) -- MatchError

    println(aPartialFunction.isDefinedAt(37))  // we can check whether the partial function is defined at 37 or not
    val liftedPF : Int => Option[Int] = aPartialFunction.lift  // converting Partial function to Int => Option[Int]
    println(liftedPF(5))  // we get Some(999)
    println(liftedPF(37))  // in this case we get None

    val anotherPartialFunction : PartialFunction[Int, Int] = {
      case 45 => 86
      case 67 => 998
    }

    val pfChain = aPartialFunction.orElse(anotherPartialFunction)
    println(pfChain(45))

    // Higher Order functions accept Partial Functions as arguments
    val aList = List(1,2,3,4)
    val aChangedList = aList.map(x => x match {
      case 1 => 4
      case 2 => 3
      case 3 => 45
      case 4 => 67
      case _ => 0
    })
    println(aChangedList)

    val aChangedListPartialFunction = aList.map({ // possible because PartialFunction[A,B] extends Function1[A,B]
      case 1 => 40
      case 2 => 30
      case 3 => 450
      case 4 => 670
      case _ => 0
    })
    println(aChangedListPartialFunction)

    val aChangedListPartialFunction_v2 = aList.map {
      case 1 => 400
      case 2 => 300
      case 3 => 4500
      case 4 => 6700
      case _ => 0
    }
    println(aChangedListPartialFunction_v2)

    case class Person(name : String, age : Int)
    val someKids = List(
      Person("Alice", 3),
      Person("Bobby", 5),
      Person("Jane", 4)
    )

    val kidsGrowingUp = someKids.map(kid => Person(kid.name, kid.age + 1))
    println(kidsGrowingUp)

    val kidsGrowingUp_v2 = someKids.map {
      case Person(name, age) => Person(name, age + 1)
    }
    println(kidsGrowingUp_v2)
  }
}
