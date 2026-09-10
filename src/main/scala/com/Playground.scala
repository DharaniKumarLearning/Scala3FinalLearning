package com


sealed abstract class LibraryItem(val isbn: String) {
  def render(): String
}
case class Book(title: String, author: String, publicationYear: Int, override val isbn: String) extends LibraryItem(isbn) {
  override def render(): String = s"The book has title $title and the author is $author. The publication year is $publicationYear with isbn $isbn"
}

case class Magazine(title: String, issueNumber: Int, month: Int, override val isbn: String) extends LibraryItem(isbn) {
  override def render(): String = s"The magazine has title $title, IssueNumber : $issueNumber, Month : $month and isbn : $isbn"
}


case class Audiobook(title: String, author: String, narrator: String, durationInMinutes: Int, override val isbn: String) extends LibraryItem(isbn) {
  override def render(): String = s"The audio book has title $title, narrator : $narrator, durationInMinutes : $durationInMinutes and isbn : $isbn"
}

case class Library(items: List[LibraryItem]) {

  def add(item: LibraryItem): Either[String, Library] =
    if(items.exists(_.isbn == item.isbn))
      Left(s"The isbn ${item.isbn} already exists in the library hence not adding it")
    else
      Right(Library(items :+ item))

  def addAll(items: List[LibraryItem]): (List[LibraryItem], String) =

    val cleanedItems = items.tail.foldLeft[(List[LibraryItem], String)]((List(items.head), ""))((acc,value) =>
      if(acc._1.exists(_.isbn == value.isbn)) (acc._1, acc._2 + value.isbn + ",")
      else (acc._1 :+ value, acc._2)
    )
    if(cleanedItems._1.length == items.length) {
      val existingIsbns = this.items.map(_.isbn).toSet & items.map(_.isbn).toSet
      if(existingIsbns.nonEmpty)
        (this.items, existingIsbns.mkString(","))
      else
        (cleanedItems._1 ++ this.items, "")
    } else (this.items, cleanedItems._2)

}

object Playground {

  def main(args: Array[String]): Unit = {

    val book1 = Book("Book1", "Andrew Hunt", 1999, "978-0201616224")
    val book2 = Book("Book2", "Martin Odersky", 2001, "978-0201616225")

    val magazine1 = Magazine("magazine1", 1, 1, "978-0201616226")
    val magazine2 = Magazine("magazine2", 2, 2, "978-0201616227")

    val audioBook1 = Audiobook("AudioBook1","author1", "narrator1", 10, "978-0201616228")
    val audioBook2 = Audiobook("AudioBook2","author2", "narrator2", 10, "978-0201616228")
    val library = Library(Nil)
    val updated = library.addAll(List(audioBook1,audioBook2, magazine1, magazine2, book1, book2))
    if(updated._2.nonEmpty) println(updated._2)
    else println(updated._1)

  }
}
