package com.ClaudeQuestions

import scala.annotation.tailrec

  object Batch3Questions {
  def main(args: Array[String]) : Unit = {

    // Question 21 -- foldLeft will return 15 and foldRight will return 3

    // List(1,1,2,2,2,3,1,1)

    def compress[A](list: List[A]): List[A] = {
      list.reverse.foldLeft(List.empty) { (acc,x) =>
        if(acc.isEmpty || acc.head != x) x :: acc
        else acc
      }
    }

    println(compress(List(1,1,2,2,2,3,1,1)))

    val sentences : List[String] = List("Dharani is good boy", "Kavya is good girl", "Mincy is their daughter")
    println(sentences.flatMap(sentence => sentence.split(" ")).map(x => (1,x)).groupBy((k,v) => v).map((k,v) => (k,v.length)))

    def interleave[A](l1: List[A], l2: List[A]): List[A] = {
      @tailrec
      def interleaveHelper(list1: List[A], list2: List[A], acc: List[A]) : List[A] = {
        if(list1.isEmpty && list2.isEmpty) acc
        else if(list1.isEmpty && list2.nonEmpty) interleaveHelper(list1, list2.tail, list2.head :: acc)
        else if(list1.nonEmpty && list2.isEmpty) interleaveHelper(list1.tail, list2, list1.head :: acc)
        else interleaveHelper(list1.tail, list2.tail, list2.head :: list1.head :: acc)
      }
      interleaveHelper(l1, l2, List.empty).reverse
    }

    println(interleave(List(1,2,3), List("a","b","c")))
    println(interleave(List(1,2,3,4,5), List("a","b","c")))
    println(interleave(List(1,2,3,4,5), List("a","b","c", "d", "e", "f", "g")))

    // Question 25 -- 1a, 1b, 2a, 2b, 3a, 3b

    def partition[A](list: List[A])(predicate: A => Boolean): (List[A], List[A]) = {
      list.reverse.foldLeft((List[A](), List[A]())){(acc,x) =>
        if(predicate(x)) (x :: acc._1, acc._2)
        else (acc._1, x :: acc._2)
      }
    }

    println(partition(List(1,2,3,4,5))(_ % 2 == 0))
    println(partition(List(1,2,3,4,5))(_ % 5 == 0))

    val m = Map("a" -> List(1,2), "b" -> List(3,4), "c" -> List(5))
    println(m.map((k,v) => v).flatMap(x => x).filter(_ % 2 == 0))

    def zipWithIndex[A](list: List[A]): List[(A, Int)] = {
      @tailrec
      def zipWithIndexHelper(l : List[A], counter: Int, acc: List[(A, Int)]) : List[(A, Int)] = {
        if(l.isEmpty) acc
        else zipWithIndexHelper(l.tail,counter + 1, (l.head, counter) :: acc)
      }
      zipWithIndexHelper(list, 0, List[(A,Int)]()).reverse
    }

    println(zipWithIndex(List("a","b","c")))

    val pairsOf30 = for {
      x <- 1 to 10
      y <- 1 to 10
      if x * y == 30
    } yield (x,y)

    println(pairsOf30.toList)

    // Question 30 -- List(1,10,2,20,3,30) without flatMap it returns List(List(1,10), List(2,20), List(3,30))

  }
}
