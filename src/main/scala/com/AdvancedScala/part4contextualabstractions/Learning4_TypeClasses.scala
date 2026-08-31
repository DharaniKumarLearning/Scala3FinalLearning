package com.AdvancedScala.part4contextualabstractions

object Learning4_TypeClasses {

  // type class definition
  trait HTMLSerializer[T] {
    def serialise(value: T) : String
  }

  // type class instances
  case class User(name: String, age: Int, email: String)
  given userSerializer: HTMLSerializer[User] with
    override def serialise(user: User): String =
      val User(name, age, email) = user
      s"<div>$name ($age yo) <a href=$email/></div>"

  import java.util.Date
  given dateSerializer: HTMLSerializer[Date] with
    override def serialise(date: Date): String =
      s"<div>${date.toString}</div>"


  // using the type class user facing API
  private object HTMLSerializer {
    def serialise[T](value: T)(using htmlSerializer: HTMLSerializer[T]): String = htmlSerializer.serialise(value)
    def apply[T](using htmlSerializer: HTMLSerializer[T]) : HTMLSerializer[T] = htmlSerializer
  }

  private object HTMLSyntax {
    extension [T](value: T) {
      def toHTML(using htmlSerializer: HTMLSerializer[T]) : String = htmlSerializer.serialise(value)
    }
  }


  def main(args: Array[String]): Unit = {
    val mincy = User("Mincy", 2, "mincy@apple.com")
    println(userSerializer.serialise(mincy))
    println(dateSerializer.serialise(Date()))
    println(HTMLSerializer.serialise(mincy))
    println(HTMLSerializer.serialise(Date()))
    println(HTMLSerializer[User].serialise(mincy))

    import HTMLSyntax.*
    println(mincy.toHTML)
    println(Date().toHTML)
  }

}
