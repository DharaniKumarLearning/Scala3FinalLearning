package com.AdvancedScala.part1as

import scala.util.Try

object Learning1_DarkSugars {
  def main(args: Array[String]): Unit = {

    // 1 - sugars for methods with one argument
    def singleArgMethod(arg : Int) : Int = arg + 1
    val aMethodCall = singleArgMethod({
      println("Calling singleArgMethod with the argument as 42")
      42
    })
    println(aMethodCall)

    val aMethodCall_v2 = singleArgMethod { // we don't need to use parentheses we can directly pass braces
      println("Calling singleArgMethod with the argument as 42")
      42
    }
    println(aMethodCall_v2)

    val aTryInstance = Try { // we can use the same syntax while creating Try instance as well
      throw new RuntimeException("Just some exception")
    }
    println(aTryInstance)

    // we can use the same syntax with higher order functions as well
    val anIncrementedList = List(1,2,3,4).map { x =>
      val result = x + 1
      result + 2
    }
    println(anIncrementedList)

    // 2 - single abstract method pattern (since Scala 2.12)
    trait Action {
      def act(x : Int) : Int
    }

    val anAction = new Action {
        override def act(x: Int): Int = x + 1
    }
    println(anAction.act(10))

    val anotherAction : Action = (x:Int) => x + 1  // the compiler will pass the body of lambda to body of the act function
    println(anotherAction.act(10))

    // where single abstract pattern is actually used
    val aThread = new Thread(new Runnable {
      override def run(): Unit = println("Hi Scala from another thread")
    })
    aThread.run()

    val aSweeterThread = new Thread(() => println("Hi, Scala"))
    aSweeterThread.run()
    /*
      single abstract pattern works even when our abstract types contain implemented methods as well
      but there should be only one abstract method in the abstract type we define
    */

    // 3 - methods ending in a : are right associative
    val aList = List(1,2,3)
    val aPrependedList = 0 :: aList  // the :: method is of List not of 0
    println(aPrependedList)
    val anotherPrependedList = aPrependedList.::(-1)
    println(anotherPrependedList)
    val aBigList = 0 :: 1 :: 2 :: List(3,4)  // List(3,4).::(2).::(1).::(0)
    println(aBigList)

    class MyStream[T] {
      infix def -->:(value : T) : MyStream[T] = this  // implementation not important
    }

    val myStream = 1 -->: 2 -->: 3 -->: 4 -->: new MyStream[Int]  // the methods we define ending with : are also right associative
    println(myStream)

    // 4 - multi-word identifiers
    class Talker(name : String) {
      infix def `and then said`(gossip : String) : Unit = println(s"$name said $gossip")
    }

    val talker = new Talker("Dharani")
    talker `and then said` "I love Scala"

    // this type of naming convention is usually used in HTTP libraries
    object `Content-Type` {  // usually hyphen(-) not allowed in names but with back tick we can have it
      val `application/json` = "application/JSON"
    }

    // 5 - infix types
    import scala.annotation.targetName
    @targetName("Arrow")  // in the byte code the Scala code generated this class can be referred as Arrow
    infix class -->[A,B]
    val compositeType : -->[Int, String] = new -->[Int, String]
    val compositeType2 : Int --> String = new -->[Int, String]  // since we defined class as infix we can use Int --> String syntax
    // this is just like new Function1[Int,Int] gets converted to Int => Int

    // 6 - update()
    val anArray = Array(1,2,3,4)
    anArray.update(2,45)
    anArray(1) = 100  // internally this syntax calls update method
    println(anArray.toList)

    // 7 - mutable fields
    class Mutable {
      private var internalMember : Int = 0
      def member : Int = internalMember  // getter method
      def member_=(value : Int) : Unit = internalMember = value  // setter method
    }

    val aMutableContainer = new Mutable
    aMutableContainer.member = 42  // aMutableContainer.member_=(42)
    // setter and getter method should be of same name for this syntax to work
    println(aMutableContainer.member)

    // 8 - variable arguments (var args)
    def methodWithVarArgs(args : Int*) = args.sum
    println(methodWithVarArgs())
    println(methodWithVarArgs(1))
    println(methodWithVarArgs(1, 2))
    println(methodWithVarArgs(1, 2, 3))

    val aCollection = List(1,2,3,4)
    println(methodWithVarArgs(aCollection*))

  }
}
