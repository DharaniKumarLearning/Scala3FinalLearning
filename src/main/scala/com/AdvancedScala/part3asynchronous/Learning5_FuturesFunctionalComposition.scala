package com.AdvancedScala.part3asynchronous

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext
import scala.util.Random
import scala.concurrent.Future
import scala.util.{Success,Failure}

object Learning5_FuturesFunctionalComposition {

  val executors : ExecutorService= Executors.newFixedThreadPool(4)
  given executionContext : ExecutionContext = ExecutionContext.fromExecutorService(executors)

  case class Profile(id: String, name: String) {
    def sendMessage(anotherProfile: Profile, message: String): Unit = {
      println(s"${this.name} sending message to ${anotherProfile.name} : $message")
    }
  }

  private object SocialNetwork {
    private val names : Map[String,String] = Map(
      "id1" -> "Dharani",
      "id2" -> "Kavya",
      "id3" -> "Mincy"
    )

    private val friends : Map[String, String] = Map("id1" -> "id3", "id2" -> "id3")
    private val random = new Random()

    def fetchProfile(id: String) : Future[Option[Profile]] = Future {
      Thread.sleep(random.nextInt(300))
      val result = for {
        name <- names.get(id)
      } yield Profile(id, name)
      result
    }

    def fetchBestFriend(profile: Profile) : Future[Option[Profile]] = Future {
      Thread.sleep(random.nextInt(500))
      val result = for {
        bestFriendId <- friends.get(profile.id)
        bestFriendName <- names.get(bestFriendId)
      } yield Profile(bestFriendId, bestFriendName)
      result
    }
  }

  private def sendMessageToBestFriend(accountId: String, message: String) : Unit = {
    // there are 3 steps involved in sending message to best friend -- fetchProfile associated with accountId, if account exists then get the best friend, then send the message
    val profileFuture = SocialNetwork.fetchProfile(accountId)
    profileFuture.onComplete {
      case Success(value) => value match {
        case Some(data) =>
          println(s"The fetchProfile completed successfully and the value returned is $data : the thread is ${Thread.currentThread().getName}")
          val fetchBestFriendProfileFuture = SocialNetwork.fetchBestFriend(data)
          fetchBestFriendProfileFuture.onComplete {
            case Success(bestFriendOption) => bestFriendOption match {
              case Some(bestFriendProfile) =>
                println(s"The fetchBestFriend completed successfully and the value returned is $bestFriendProfile : the thread is ${Thread.currentThread().getName}")
                data.sendMessage(bestFriendProfile, message)
              case None => println(s"The fetchBestFriend doesn't return any data for ${data} : the thread is ${Thread.currentThread().getName}")
            }
            case Failure(ex) => ex.printStackTrace()
          }
        case None => println(s"The names database doesn't contain the id specified : the thread is ${Thread.currentThread().getName}")
      }
      case Failure(ex) => ex.printStackTrace()
    }

    // If we have multiple futures like this tracking the Success, Failure, Some, None using onComplete is a complete hassle
    // The solution is to use functional composition on the futures
  }

  private def sendMessageToBestFriend_V2(accountId: String, message: String) : Unit = {
    val profileFuture = SocialNetwork.fetchProfile(accountId)
    profileFuture.flatMap {
      case Some(profile) => SocialNetwork.fetchBestFriend(profile).map(_.getOrElse(Profile("dummy_id", "dummy_name"))).map(bestFriendProfile => profile.sendMessage(bestFriendProfile, message))
      case None => Future.successful(Profile("dummy_id", "dummy_name"))
    }
  }

  private def sendMessageToBestFriend_V3(accountId: String, message: String) : Unit = {

    for {
      profile <- SocialNetwork.fetchProfile(accountId) if profile.nonEmpty
      bestFriendProfile <- SocialNetwork.fetchBestFriend(profile.get) if bestFriendProfile.nonEmpty
    } yield profile.get.sendMessage(bestFriendProfile.get, message)

  }

  def main(args: Array[String]): Unit = {

    sendMessageToBestFriend("id1", "Good Morning")
    sendMessageToBestFriend_V2("id2", "Good Afternoon")
    sendMessageToBestFriend_V3("id1", "Good Night")

    val dharaniProfile : Future[Option[Profile]] = SocialNetwork.fetchProfile("id1")

    // map transforms the value contained inside future asynchronously it uses the same execution context
    val dharaniFuture : Future[String] = dharaniProfile.map(profile => profile.getOrElse(Profile("dummy_id","dummy_name")).name)

    val dharaniBestFriend : Future[Profile] = dharaniProfile.flatMap {
      case Some(profile) => SocialNetwork.fetchBestFriend(profile).map(_.getOrElse(Profile("dummy_id", "dummy_name")))
      case None => Future.successful(Profile("dummy_id", "dummy_name"))
    }

    val dharaniBestFriendFilter : Future[Profile] = dharaniBestFriend.filter(profile => profile.name.startsWith("M"))

    val someData = Map("I" -> "Dharani", "II" -> "Kavya", "III" -> "Mincy")
    def getSomeData(id: String) : Future[String] = Future.apply(someData(id))

    val futureWithRecover : Future[String] = getSomeData("IV").recover {
      case e: Throwable =>
        println(s"Exception occurred while executing future $e hence returning dummy data")
        "dummy"
    }

    val futureWithRecoverWith : Future[String] = getSomeData("IV").recoverWith {
      case e: Throwable => getSomeData("V")  // if this throws exception it will get propagated
    }

    // if both the futures throw exception then the first future exception is propagated
    // this is the difference between recoverWith and fallBackTo
    val fallBackFuture : Future[String] = getSomeData("IV").fallbackTo(getSomeData("V"))

    Thread.sleep(4000)

    println(dharaniFuture.value)
    println(dharaniBestFriend.value)
    println(dharaniBestFriendFilter.value)
    println(futureWithRecover.value)
    println(futureWithRecoverWith.value)
    println(fallBackFuture.value)
    executors.shutdown()
  }
}
