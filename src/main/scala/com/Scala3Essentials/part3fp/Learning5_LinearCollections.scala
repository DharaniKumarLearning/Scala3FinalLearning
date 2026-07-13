package com.Scala3Essentials.part3fp

import scala.util.Random

object Learning5_LinearCollections {

  /*
     Seq -- a trait that describes a linear collection of objects that are ordered
     It has well-defined ordering + indexing
  */

  private def testSeq(): Unit = {
    val aSequence = Seq(1, 2, 3, 4)
    println(aSequence)
    println(aSequence.apply(2)) // Seq is 0 indexed in this case the apply method returns 3rd element
    println(aSequence.reverse) // reversing the sequence
    println(aSequence ++ Seq(10, 20, 30, 40)) // concatenation
    println(Seq(4, 2, 3, 1).sorted) // sorted

    // map, filter, flatMap and for comprehensions are available for Seq data type
    println(aSequence.map(_ + 2))
    println(aSequence.filter(_ % 2 == 0))
    println(aSequence.flatMap(x => List(x, x + 1)))
    for {
      value <- aSequence
    } println(value)
    println(aSequence.foldLeft(0)((x,y) => x + y * 2))
    println(aSequence.mkString(","))
    println(aSequence.mkString("[",",","]"))
  }

  private def testList() : Unit = {
    val aList = List(1,2,3,4)
    // List has same API as Seq
    println(aList.head)  // first element
    println(aList.tail)  // remaining elements
    println(0 :: aList)  // prepending to a List :: this is called cons operator which works only on List
    println(10 +: aList)  // this is also prepending, but it is generic available for all linear collections
    println(0 +: aList :+ 5) // prepending and appending to List at the same time
    println(List.fill(5)("Scala"))  // creates a List with the value Scala 5 times
  }

  private def testRange() : Unit = {
    val aRange : Seq[Int] = 1 to 10
    (1 to 10).foreach(x => println("Scala"))
    (1 until 10).foreach(x => println(x))  // the end value of the range is not inclusive
  }

  private def testArray() : Unit = {
    val anArray = Array(1,2,3,4,5,6)
    // Arrays are not sequences
    println(anArray.toIndexedSeq)
    anArray.update(2,30)  // Arrays are mutable
    println(anArray.mkString(","))  // no new array is allocated
  }

  private def testVector() : Unit = {
    // Vectors are fast sequences for a large amount of data
    // Vectors are actually the default recommended sequence in Scala for most cases
    // List wins only when you are heavily doing prepend + head/tail operations like in recursive algorithms
    val aVector = Vector(1,2,3,4,5,6)
    println(aVector)
  }

  private def smallBenchMark() : Unit = {
    val maxRuns = 1000
    val maxCapacity = 1000000

    def getWriteTime(collection : Seq[Int]) : Double = {
      val random = new Random()
      val times = for {
        i <- 1 to maxRuns
      } yield {
        val index = random.nextInt(maxCapacity)
        val element = random.nextInt()
        val currentTime = System.nanoTime()
        val updatedCollection = collection.updated(index, element)
        System.nanoTime() - currentTime
      }
      times.sum / maxRuns
    }

    val numbersList = (1 to maxCapacity).toList
    val numbersVector = (1 to maxCapacity).toVector
    println(s"List update time : ${getWriteTime(numbersList)}")
    println(s"Vector update time : ${getWriteTime(numbersVector)}")
  }

  private def testSet() : Unit = {
    val aSet = Set(1,2,3,4,4,5)  // no ordering guaranteed
    println(aSet)
    println(aSet.contains(3))  // to know whether an element exists in Set or not
    println(aSet(3))  // the apply method on Set companion also does the same thing
    println(aSet + 6)  // adding an element to Set
    println(aSet - 4)  // removing an element from Set
    println(aSet ++ Set(10,20,30,40))  // adding all the elements from another Set
    println(aSet.union(Set(100,200,300,400,500)))
    println(aSet | Set(1000,2000,3000,4000))
    println(aSet.diff(Set(3,4,5,6,7)))
    println(aSet -- Set(3,4,5,2,3,5,6,7))  // same as diff
    println(aSet.intersect(Set(3,4,5,6)))
    println(aSet & Set(3,4,5,6))  //  same as intersect
  }

  def main(args: Array[String]): Unit = {

    testSeq()
    testList()
    testRange()
    testArray()
    testVector()
    smallBenchMark()
    testSet()

  }
}
