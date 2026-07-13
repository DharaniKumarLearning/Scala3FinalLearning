package com.AdvancedScala.part3asynchronous

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}


object Learning4_Futures {

  def calculateMeaningOfLife(): Int = {
    Thread.sleep(1000)
    42
  }

  // thread-pool (java specific)
  val executor: ExecutorService = Executors.newFixedThreadPool(4)

  // thread-pool (scala specific)
  given anExecutionContext : ExecutionContext = ExecutionContext.fromExecutorService(executor)

  // a future is asynchronous computation that will finish at some point
  // we can remove (anExecutionContext) since we made anExecutionContext given it will get passed here
  val aFuture : Future[Int] = Future.apply(calculateMeaningOfLife())(anExecutionContext)

  // val aFuture : Future[Int] = Future.apply(calculateMeaningOfLife()) -- we can have this line as well

  // the type is Option[Try[Int]] because 1. we don't know whether we have a value if we do that could be a failed one
  val futureInstantResult : Option[Try[Int]] = aFuture.value

  // callbacks
  aFuture.onComplete {
    case Success(value) => println(s"I have completed meaning of life and the returned value is $value")
    case Failure(exception) => println(s"my async computation failed $exception")
  } // can not guarantee on which thread this might run

  case class Profile(id: String, name: String) {
    def sendMessage(anotherProfile: Profile, message: String) : Unit =
      println(s"${this.name} sending message to ${anotherProfile.name} : $message")
  }

  object SocialNetwork {

    val names : Map[String,String] = Map(
      "rtjvm.id.1-daniel" -> "Daniel",
      "rtjvm.id.2-jane" -> "Jane",
      "rtjvm.id.3-mark" -> "Mark"
    )

    // friends database
    val friends : Map[String,String] = Map(
      "rtjvm.id.2-jane" -> "rtjvm.id.3-mark"
    )

    val random = new Random()

    def fetchProfile(id: String) : Future[Profile] = Future {
      println(s"This fetchProfile future is executing on ${Thread.currentThread().getName}")
      Thread.sleep(random.nextInt(300))
      Profile(id,names(id))
    }

    def fetchBestFriend(profile: Profile) : Future[Profile] = Future {
      println(s"This fetchBestFriend future is executing on ${Thread.currentThread().getName}")
      Thread.sleep(random.nextInt(400))
      val bestFriendId = friends(profile.id)
      Profile(bestFriendId, names(bestFriendId))
    }

  }

  // problem : sending a message to my best friend
  def sendMessageToBestFriend(accountId : String, message : String) : Unit = {
    // 1 - call fetchProfile
    // 2 - get bestFriend
    // call profile.sendMessage(bestFriend)

    val profileFuture = SocialNetwork.fetchProfile(accountId)
    profileFuture.onComplete {
      case Success(profile) =>
        val friendProfileFuture = SocialNetwork.fetchBestFriend(profile)
        friendProfileFuture.onComplete {
          case Success(friendProfile) => profile.sendMessage(friendProfile, message)
          case Failure(ex) => ex.printStackTrace()
        }
      case Failure(exception) => exception.printStackTrace()
    }

    // onComplete is a hassle
    // solution : functional composition
  }

  def sendMessageToBestFriend_v2(accountId : String, message : String) : Unit = {
    val profileFuture = SocialNetwork.fetchProfile(accountId)
    val action = profileFuture.flatMap { profile =>
      SocialNetwork.fetchBestFriend(profile).map {
        bestFriend => profile.sendMessage(bestFriend, message)  // Unit
      }
    }
  }

  def sendMessageToBestFriend_v3(accountId : String, message : String) : Unit = {
    val action = for {
      profileFuture <- SocialNetwork.fetchProfile(accountId)
      bestFriendProfile <- SocialNetwork.fetchBestFriend(profileFuture)
    } yield profileFuture.sendMessage(bestFriendProfile, message)
  }

  def main(args: Array[String]): Unit = {
    println(futureInstantResult)

//    val janeProfileFuture = SocialNetwork.fetchProfile("rtjvm.id.2-jane")
//    val janeFuture : Future[String] = janeProfileFuture.map(profile => profile.name)  // map transforms value contained inside a container asynchronously
//    val janesBestFriend : Future[Profile] = janeProfileFuture.flatMap(profile => SocialNetwork.fetchBestFriend(profile)) // flatMap is executed asynchronously
//    val janesBestFriendFilter : Future[Profile]  = janesBestFriend.filter(profile => profile.name.startsWith("Z")) // filter is also executed asynchronously

//    sendMessageToBestFriend("rtjvm.id.2-jane", "Hey best friend nice to talk to you again")
//    sendMessageToBestFriend_v2("rtjvm.id.2-jane", "Hey best friend nice to talk to you again version2")
//    sendMessageToBestFriend_v3("rtjvm.id.2-jane", "Hey best friend nice to talk to you again version3")

    val profileNoMatterWhat = SocialNetwork.fetchProfile("unknown-id").recover {
      case e: Throwable => Profile("rtjvm.id.0.dummy", "Forever alone")
    }

    // second exception is thrown
    val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown-id").recoverWith {
      case e: Throwable => SocialNetwork.fetchProfile("rtjvm.id.0.dummy")
    }

    // first future exception is thrown
    val fallBackProfile = SocialNetwork.fetchProfile("unknown-id").fallbackTo(SocialNetwork.fetchProfile("rtjvm.id.0.dummy"))

    Thread.sleep(2000)
    println(profileNoMatterWhat)
    println(aFetchedProfileNoMatterWhat)
    println(fallBackProfile)
    executor.shutdown()
  }
}
