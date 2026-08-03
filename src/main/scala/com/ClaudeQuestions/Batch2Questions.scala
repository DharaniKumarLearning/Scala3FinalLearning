package com.ClaudeQuestions

object Batch2Questions {

  class Counter(val count: Int) {
    def increment: Counter = new Counter(count + 1)
    def decrement: Counter = new Counter(count - 1)
    def increment(n: Int): Counter = new Counter(count + n)
    def decrement(n: Int): Counter = new Counter(count - n)
  }

  trait Describable {
    def describe: String
  }

  case class Dog(name: String, age: Int) extends Describable {
    override def describe: String = s"this is a dog with name $name and its age is $age"
  }

  case class Cat(name: String, indoor: Boolean) extends Describable {
    override def describe: String = s"this is a cat with name $name and does it stay indoor $indoor"
  }

  sealed trait Shape
  case class Circle(radius: Double) extends Shape
  case class Rectangle(width: Double, height: Double) extends Shape
  case class Triangle(base: Double, height: Double) extends Shape

  class Pair[A, B](val first: A, val second: B) {
    def swap : Pair[B, A] = new Pair(second, first)
    def map[C](f: A => C): Pair[C, B] = new Pair(f(first), second)
  }

  class Person(val name: String, val age: Int)
  object Person {
    def apply(name: String, age: Int) = new Person(name, age)
    def oldest(people: List[Person]): Person = people.maxBy(p => p.age)
  }

  enum Direction {
    case North, South, East, West
    def opposite: Direction = this match
      case North => South
      case South => North
      case East => West
      case West => East
  }

  case class Student(name: String, grade: Int)

  abstract class Stack[A] {
    def push(element: A): Stack[A]
    def pop: (A, Stack[A])
    def isEmpty: Boolean
  }

  case class EmptyStack[A]() extends Stack[A] {
    override def push(element: A): Stack[A] = NonEmptyStack(element, this)
    override def pop: (A, Stack[A]) = throw new NoSuchElementException("pop from empty stack")
    override def isEmpty: Boolean = true
  }

  case class NonEmptyStack[A](head: A, tail: Stack[A]) extends Stack[A] {
    override def push(element: A): Stack[A] = NonEmptyStack(element, this)
    override def pop: (A, Stack[A]) = (head, tail)
    override def isEmpty: Boolean = false
  }

  def main(args: Array[String]): Unit = {

    val counter = new Counter(10)
    println(counter.increment.increment(2).decrement(3).count)

    val dog = Dog("German Shepherd", 4)
    val anotherDog = Dog("SomeDog", 5)
    val cat = Cat("SomeCat", true)

    val describableAnimals : List[Describable] = List(dog,cat, anotherDog)
    describableAnimals.foreach {
      case animal@Dog(name, age) => println(animal.describe)
      case animal@Cat(name, indoor) => println(animal.describe)
    }

    def area(shape: Shape): Double = shape match {
      case Circle(radius) => Math.PI * radius * radius
      case Rectangle(w, h) => w * h
      case Triangle(b, h) => 0.5 * b * h
    }

    val rectangle = Rectangle(10,20)
    println(area(rectangle))

    val pair = new Pair("Zero", 0)
    println(pair.swap.first)
    println(pair.swap.second)
    println(pair.map((x: String) => x.charAt(pair.second)).first)

    val firstPerson = Person("Dharani", 30)
    val secondPerson = Person("Kavya", 29)
    println(Person.oldest(List(firstPerson, secondPerson)).name)

    // Question 16 : the Dog class's toString will get executed

    val direction = Direction.East
    println(direction.opposite)

    val student1 = Student("Dharani", 80)
    val student2 = Student("Kavya", 90)
    val student3 = Student("Mincy", 70)
    val studentList = List(student1, student2, student3)

    val improvedStudentList = studentList.map(student => student.copy(grade = student.grade + 10))

    // We can do the same thing easily using partial function
    val improvedStudentList2 = studentList.map {
      case Student(name, grade) => Student(name, grade + 10)
    }

    println(improvedStudentList)
    println(improvedStudentList2)

    // Question 19 -- We can extend like that we should with while extending from traits since we have already extended from Animal

    val stack = NonEmptyStack(1, NonEmptyStack(2, NonEmptyStack(3, EmptyStack())))
    println(stack)
    println(stack.push(4))
    println(stack.push(4).pop)

  }
}
