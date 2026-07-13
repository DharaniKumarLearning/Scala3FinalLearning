package com.Scala3Essentials.part3fp

import scala.util.Random

object Learning8_Options {

  // Option -- mini collection with at-most one value

  def main(args: Array[String]): Unit = {

    val anOption : Option[Int] = Option(42)
    val anEmptyOption : Option[Int] = Option.empty

    // Why do we need Options? The goal is to work with unsafe APIs
    def unsafeMethod() : String = null
    def fallBackMethod() : String = "some valid result"

    // defensive style we check what is the return value from the method and take appropriate action
    val stringLength = if(unsafeMethod() == null) -1 else unsafeMethod().length
    val stringLengthOption = Option(unsafeMethod()).getOrElse("The method returned null value")
    println(stringLengthOption)

    // The subtypes of Options is Some and None
    val aPresentValue : Option[Int] = Some(4)
    val anEmptyOptionV2 : Option[Int] = None
    println(anEmptyOptionV2.isEmpty)
    println(anOption.getOrElse(90))  // getting the value from Option if the Option is None the default value is returned
    // println(anEmptyOptionV2.get) -- if the Option value is None the get method returns NoSuchElementException

    // map, flatMap, filter, for
    println(anOption.map(x => x + 1))
    println(anOption.flatMap(x => Option(x + 1)))
    println(anOption.filter(x => x % 2 == 1))
    println(anEmptyOption.flatMap(x => Option(x * 10)))
    println(anEmptyOption.orElse(anOption))  // if the first Option is empty the second Option is returned
    println(Option(unsafeMethod()).orElse(Option(fallBackMethod())))

    def betterUnsafeMethod() : Option[String] = None
    def betterFallBackMethod() : Option[String] = Some("some valid result")
    println(betterUnsafeMethod().orElse(betterFallBackMethod()))

    val phoneBook = Map(
      "Daniel" -> 1234,
      "Dharani" -> 4567,
      "Jim" -> 555
    )

    println(phoneBook.get("John"))  // returns None mainly it returns Option type
    val jimNumber : Option[Int] = phoneBook.get("Jim")
    println(jimNumber)

    /*
       Exercise
       Get the host and port from config map
          try to open a connection
          print "connection successful" if connection is good else "connection failed"
    */

    val config : Map[String, String] = Map(
      // comes from elsewhere
      "host" -> "176.45.32.1",
      "port" -> "8081"
    )

    class Connection {
      def connect() : String = "Connection successful"
    }

    object Connection {
      val random = new Random()
      def apply(host : String, port : String) : Option[Connection] =
        if(random.nextBoolean()) Some(new Connection)
        else None
    }

    val host : Option[String] = config.get("host")
    val port : Option[String] = config.get("port")
    val connection = host.flatMap(h => port.flatMap(p => Connection(h,p)))
    val connStatus = connection.map((x => x.connect()))
    println(connStatus.getOrElse("connection failed"))

    val connStatus2 = for {
      h <- config.get("host")
      p <- config.get("port")
      connection <- Connection(h,p)
    } yield connection.connect()

    println(connStatus2.getOrElse("connection failed"))

  }
}
