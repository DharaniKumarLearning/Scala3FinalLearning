package com.Scala3Essentials.part2oop

import com.Scala3Essentials.DataStructureCreation.*
import java.util.List as JList // Scala3 we can gave alias using as keyword In Scala2 we were using => symbol

object Learning13_PackagesImports {
  def main(args: Array[String]): Unit = {

    // We can access the classes, objects, traits etc., defined inside other packages using fully qualified name like this
    val dharaniList : com.Scala3Essentials.DataStructureCreation.LinkedList[Int] = new NonEmptyLinkedList[Int](10, new EmptyLinkedList[Int])
    println(dharaniList.head)

    // If we import the classes directly to our file then we can use simple name
    val dharaniListV2 : LinkedList[String] = new NonEmptyLinkedList[String]("Hello", new NonEmptyLinkedList[String]("World", new EmptyLinkedList[String]))
    println(dharaniListV2)

    val aJavaList : JList[Int] = null

    import com.Scala3Essentials.DataStructureCreation.*  // we can import everything available in the package using *
    // default imports -- scala.*, scala.Predef.*, java.lang.*

  }
}
