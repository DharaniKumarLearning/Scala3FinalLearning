package com.Scala3Essentials.part3fp

import scala.util.{Failure, Random, Success, Try}

object Learning9_HandlingExceptions {
  def main(args: Array[String]): Unit = {

    // Try - a potentially failed computation
    val aTry : Try[Int] = Try(42)
    val aFailureTry : Try[Int] = Try(throw new RuntimeException()) // Try's apply method takes named argument
    println(aTry)
    println(aFailureTry)

    val aTry2 : Try[Int] = Success[Int](42)
    val aFailureTry2 : Try[Int] = Failure[Int](new RuntimeException()) // Failure apply method excepts a Throwable exception
    println(aTry2)
    println(aFailureTry2)

    println(aTry2.isSuccess)
    println(aTry2.isFailure)

    // map, flatMap, filter and for comprehension
    println(aTry.map(x => x + 1))
    println(aFailureTry.map(x => x + 1))
    println(aTry.flatMap(x => Try(x * 10)))
    println(aTry.filter(x => x > 50))
    println(aFailureTry.orElse(aTry))

    // Why Try? avoid unsafe APIs which can throw exceptions
    def unsafeMethod() : String =
      throw new RuntimeException("No string for you, buster!")

    // defensive way of handling this code is to use try/catch/finally block
    val stringLengthDefensive = try {
      val aString = unsafeMethod()
      aString.length
    } catch {
      case e : RuntimeException => -1
    }
    println(stringLengthDefensive)

    val stringLengthPure = Try(unsafeMethod()).map(_.length).getOrElse(-1)
    println(stringLengthPure)

    def betterUnsafeMethod() : Try[String] =
      if(new Random().nextBoolean()) Failure(RuntimeException("No String for you buster!"))
      else Success("Mincy")

    def betterBackupMethod() : Try[String] = Success("Scala is great")

    val stringLengthPureV2 = betterUnsafeMethod().map(_.length)
    println(stringLengthPureV2)
    println(betterUnsafeMethod().orElse(betterBackupMethod()))

    /*
     Exercise
     From the below setup
        obtain a connection
        then fetch the url
        then print the resulting html page
    */
    val host = "localhost"
    val port = "8081"
    val myDesiredUrl = "rockthejvm.com/home"

    class Connection {
      val random = new Random()
      def get(url : String) : String = {
        if(random.nextBoolean()) "<html>Success</html>"
        else throw new RuntimeException("Cannot fetch page right now")
      }
      def getSafe(url : String) : Try[String] = Try(get(url))
    }

    object HttpService {
      val random = new Random()
      def getConnection(host : String, port : String) : Connection =
        if(random.nextBoolean()) new Connection
        else throw new RuntimeException("cannot access host/port combination")

      def getConnectionSafe(host : String, port : String) : Try[Connection] =
        Try(getConnection(host, port))
    }

    val mayBeHtml : Try[String] = Try(HttpService.getConnection(host, port)).flatMap(x => Try(x.get(myDesiredUrl)))
    val finalResult = mayBeHtml.fold(e => s"${e.getMessage}", s => s)  // checking the Failure and Success case for Try
    println(finalResult)

    val mayBeHtmlPure = HttpService.getConnectionSafe(host, port).flatMap(conn => conn.getSafe(myDesiredUrl))
    println(mayBeHtmlPure.getOrElse("No webpage for you"))

    val mayBeHtmlPure_v2 = for {
      conn <- HttpService.getConnectionSafe(host, port)
      html <- conn.getSafe(myDesiredUrl)
    } yield html

    println(mayBeHtmlPure_v2)


  }
}
