package com.AdvancedScala.part3asynchronous

import scala.collection.parallel.*
import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.immutable.ParVector

object Learning9_ParallelCollections {

  val aList : List[Int] = (1 to 1000000).toList
  val anIncrementedList : List[Int] = aList.map(_ + 1)
  val parallelList : ParSeq[Int] = aList.par  // converting a normal collection to parallel collection
  val aParallelizedIncrementedList: ParSeq[Int] = parallelList.map(_ + 1)  // we have another methods like map, flatMap, filter, foreach, reduce, fold for parallel collections also

  val aParallelVector : ParVector[Int] = ParVector[Int](1,2,3,4,5)

  def measure[A](expression: => A): Long = {
    val startTime = System.currentTimeMillis()
    expression
    System.currentTimeMillis() - startTime
  }

  def compareListTransformation() : Unit = {
    val list = (1 to 10000000).toList
    println("list creation done")

    val serialTime = measure(list.map(_ + 1))
    println(s"serial time : $serialTime")

    val parallelTime = measure(list.par.map(_ + 1))
    println(s"parallel time : $parallelTime")
  }

  def demoUndefinedOrder() : Unit = {
    val aList = (1 to 1000).toList
    val reduction = aList.reduce((x,y) => x - y)  // usually bad idea to use non-associative operators
    val parallelReduction = aList.par.reduce((x,y) => x - y)  // order of operations is undefined, returns different result

    println(s"Sequential Reduction : $reduction")
    println(s"Parallel Reduction : $parallelReduction")
  }

  def demoDefinedOrder() : Unit = {
    val strings = "I love parallel collections but I must be careful".split(" ").toList
    val concatenation = strings.reduce((x,y) => x + " " + y)  // for associative operations result is deterministic
    val parallelConcatenation = strings.par.reduce((x,y) => x + " " + y)
    println(s"Sequential concatenation = $concatenation")
    println(s"Parallel Concatenation = $parallelConcatenation")
  }

  // be careful with imperative programming on parallel collections
  def demoRaceConditions() : Unit = {
    var sum = 0
    (1 to 1000).toList.par.foreach(elem => sum += elem)
    println(sum)
  }

  def main(args: Array[String]): Unit = {
    compareListTransformation()
    demoUndefinedOrder()
    demoDefinedOrder()
    demoRaceConditions()
  }
}
