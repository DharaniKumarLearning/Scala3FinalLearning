package com.AdvancedScala.part2advancedfp

object Learning3_CurryingAndPAFs {
  def main(args: Array[String]): Unit = {

    val superAdder : Int => Int => Int = x => y => x + y
    val add3 : Int => Int = superAdder(3)
    println(add3(5))
    println(superAdder(3)(5))

    def curriedAdder(x : Int)(y : Int) : Int = x + y
    // methods != function values
    val add4 : Int => Int = curriedAdder(4)
    // when we write the above line the compiler does something known as eta-expansion which will convert method to curried function
    println(add4(5))

    def anIncrement(x : Int) : Int = x + 1
    val aList = List(1,2,3,4)
    println(aList.map(anIncrement))  // here the compiler does eta-expansion and converts our method to function type

    // underscores are powerful
    def concatenate(a : String, b : String, c : String) : String = a + b + c

    val insertName : String => String = concatenate(
      "Hello, my name is ",
      _:String,
      "I am going to show you nice scala trick"
    ) // once we give underscore like this it will convert the function call into lambda
    // x => concatenate("Hello, my name is",x,"I am going to show you nice scala trick")

    println(insertName("Dharani "))

    val fillInTheBlanks = concatenate(_: String, "Dharani", _ : String)  // (x,y) => concatenate(x,"Dharani", y)
    println(fillInTheBlanks("My name is ", ". I am 30 years old"))

    /*
       Exercises
        1. Create as many add7 definitions
        2. Process a List of numbers and return their string representations under different formats
            a) Create a curried formatting method with a formatting string and value
            b) process a list of numbers with various formats
    */

    val simpleAddFunction = (x : Int, y : Int) => x + y
    def simpleAddMethod(x : Int, y : Int) : Int = x + y
    def curriedMethod(x : Int)(y : Int) = x + y

    val add7 = (x:Int) => simpleAddFunction(x,7)
    val add7_v2 = (x:Int) => simpleAddMethod(x, 7)
    val add7_v3 = (x:Int) => curriedMethod(x)(7)
    val add7_v4 = curriedAdder(7)
    val add7_v5 = simpleAddMethod(_:Int,7)
    val add7_v6 = simpleAddFunction.curried(7)

    println(add7(10))
    println(add7_v2(11))
    println(add7_v3(12))
    println(add7_v4(13))
    println(add7_v5(14))
    println(add7_v6(15))

    val piWith2Dec = "%4.2f".format(Math.PI)
    println(piWith2Dec)

    def curriedFormatter(fmt : String)(number : Double) : String = fmt.format(number)
    val someDecimals = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)
    println(someDecimals.map(curriedFormatter("%4.2f")))  // x => curriedFormatter("%4.2f")(x)
    println(someDecimals.map(curriedFormatter("%8.6f")))
    println(someDecimals.map(curriedFormatter("%14.12f")))

  }
}
