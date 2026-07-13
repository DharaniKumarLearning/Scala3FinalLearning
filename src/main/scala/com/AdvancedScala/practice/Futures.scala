package com.AdvancedScala.practice

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Random, Success}


object Futures {
  def main(args: Array[String]): Unit = {

    def meaningOfLife() : Int = {
      println("This method sleeps for 1 second and then returns an Int")
      Thread.sleep(1000)
      42
    }

    val executors: ExecutorService = Executors.newFixedThreadPool(4)
    given executionContext : ExecutionContext = ExecutionContext.fromExecutorService(executors)
    val meaningOfLifeFuture : Future[Int] = Future.apply(meaningOfLife())

    meaningOfLifeFuture.onComplete {
      case Success(value) => println(s"The future succeeded and it returned the value $value")
      case Failure(ex) => println(s"The future failed with exception $ex")
    }

    case class Profile(id : String, name : String) {
      def sendMessage(anotherProfile : Profile, message : String) : Unit =
        println(s"${this.id} sending message to ${anotherProfile.id} : $message")
    }

    object SocialNetwork {
      val names : Map[String, String] = Map("Dharani" -> "Kavya", "Shiv" -> "Swetha", "Saurajit" -> "Shruthi")
      val friends : Map[String,String] = Map("Dharani" -> "Shiv")

      def fetchProfile(id: String) : Future[Profile] = Future {
        println("This future will return a Profile based on the id passed")
        Thread.sleep(new Random().nextInt(300))
        Profile(id, names(id))
      }

      def fetchBestFriend(profile: Profile) : Future[Profile] = Future {
        println("This future will return the best friend of the profile passed")
        Thread.sleep(new Random().nextInt(400))
        val bestFriend = friends(profile.id)
        Profile(bestFriend, names(bestFriend))
      }
    }

    def sendMessageToBestFriend(accountId: String, message : String) : Unit = {
      val personProfile : Future[Profile] = SocialNetwork.fetchProfile(accountId)
      personProfile.onComplete {
        case Success(profile) =>
          val bestFriendProfileFuture : Future[Profile] = SocialNetwork.fetchBestFriend(profile)
          bestFriendProfileFuture.onComplete {
            case Success(bestFriendProfile) => profile.sendMessage(bestFriendProfile, message)
            case Failure(ex1) => println(s"Exception occurred swallowing it $ex1")
          }
        case Failure(ex) => println(s"Exception occurred swallowing it $ex")
      }
    }

    def sendMessageToBestFriend_v2(accountId: String, message: String) : Unit = {
      val personProfile : Future[Profile] = SocialNetwork.fetchProfile(accountId)
      val action : Future[Unit] = personProfile.flatMap { profile =>
        SocialNetwork.fetchBestFriend(profile).map { bestFriendProfile =>
          profile.sendMessage(bestFriendProfile, message)
        }
      }
    }

    def sendMessageToBestFriend_v3(accountId: String, message : String) : Unit = {
      val action = for {
        personProfile <- SocialNetwork.fetchProfile(accountId)
        bestFriendProfile <- SocialNetwork.fetchBestFriend(personProfile)
      } yield personProfile.sendMessage(bestFriendProfile, message)
    }

//    sendMessageToBestFriend_v2("Shiv", "How are you dude?")

    val dharaniProfileFuture = SocialNetwork.fetchProfile("Gibberish").fallbackTo {SocialNetwork.fetchProfile("Shiv")}
    val dharaniValue = dharaniProfileFuture.map(x => x.name)
    Thread.sleep(1000)
    println(dharaniValue)
    executors.shutdown()

  }
}
