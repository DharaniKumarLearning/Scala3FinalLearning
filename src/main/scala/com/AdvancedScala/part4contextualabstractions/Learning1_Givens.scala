package com.AdvancedScala.part4contextualabstractions

object Learning1_Givens {

  val aList : List[Int] = List(4,2,3,1)
  given descendingOrdering : Ordering[Int] = Ordering.fromLessThan(_ > _)

  // custom sorting
  case class Person(name: String, age: Int)
  private val people : List[Person] = List(
    Person("Dharani", 31),
    Person("Mincy", 2),
    Person("Kavya", 29)
  )

  given personOrdering: Ordering[Person] = new Ordering[Person] {
    override def compare(x: Person, y: Person): Int = x.name.compareTo(y.name)
  }

  // alternate syntax for defining given
  case class School(name: String, staff: Int)
  private val allSchools : List[School] = List(School("Noble", 45), School("Fatima", 50), School("Delhi", 34))

  given schoolOrdering : Ordering[School] with {
    override def compare(x: School, y: School): Int = x.staff.compareTo(y.staff)
  }

  // If we have Ordering of same type defined in the scope then we get double definition error

  // using classes
  trait Combinator[A] {
    def combine(x: A, y: A) : A
  }

  private def combineAll[A](list: List[A])(using combinator: Combinator[A]) : A =
    list.reduce((x,y) => combinator.combine(x,y))

  given intCombinator: Combinator[Int] with {
    override def combine(x: Int, y: Int): Int = x + y
  }

  given personCombinator: Combinator[Person] with {
    override def combine(x: Person, y: Person): Person = Person(x.name + y.name, x.age + y.age)
  }

  // context bound
  private def combineInGroupsOf3[A](list: List[A])(using combinator: Combinator[A]) : List[A] =
    list.grouped(3).map(group => combineAll(group)/* combinator passed by the compiler */).toList

  private def combineInGroupsOf3_v2[A](list: List[A])(using Combinator[A]): List[A] =  // since we are not using we can pass like this as well
    list.grouped(3).map(group => combineAll(group)).toList

  private def combineInGroupsOf3_v3[A: Combinator](list: List[A]): List[A] =  // one more syntax
    list.grouped(3).map(group => combineAll(group) /* combinator passed by the compiler */).toList

  given optionOrdering[A](using normalOrdering: Ordering[A]) : Ordering[Option[A]] = new Ordering[Option[A]]:
    override def compare(x: Option[A], y: Option[A]) : Int = (x, y) match {
      case (None,None) => 0
      case (_, None) => 1
      case (None, _) => -1
      case (Some(a), Some(b)) => normalOrdering.compare(a, b)
    }

  object OptionOrdering {
    given optionOrdering[A: Ordering]: Ordering[Option[A]] = new Ordering[Option[A]]:
      override def compare(x: Option[A], y: Option[A]): Int = (x, y) match {
        case (None, None) => 0
        case (_, None) => 1
        case (None, _) => -1
        case (Some(a), Some(b)) => summon[Ordering[A]].compare(a, b)
      }
  }

  def main(args: Array[String]): Unit = {
    println(aList.sorted) // Once we make descendingOrder as "given" it is passed implicitly to sorted method
    println(aList.sorted(descendingOrdering))  // We can pass it explicitly if we want
    println(people.sorted)
    println(allSchools.sorted)
    println(combineAll(List(1,2,3,4)))  // compiler injects intCombinator automatically
    println(combineAll(people))  // compiler injects personCombinator automatically
    println(combineInGroupsOf3(List(1,2,3,4,5,6,7,8)))
    println(combineInGroupsOf3_v2(people))
    // println(combineInGroupsOf3_v3(allSchools)) -- this does not work because we don't have Combinator[School] in scope

    val ageCombinator : Combinator[Int] = new Combinator[Int] {
      override def combine(x: Int, y: Int): Int = x * y
    }
    println(combineInGroupsOf3(List(1,2,3,4,5,6,7,8))(using ageCombinator))  // passing new value to method having parameter defined as using
    println(List(Option(10), Option(5), Option(13), None).sorted)
  }
}
