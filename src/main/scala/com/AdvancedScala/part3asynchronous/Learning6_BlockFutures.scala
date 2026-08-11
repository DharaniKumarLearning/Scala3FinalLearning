package com.AdvancedScala.part3asynchronous

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.*
import scala.util.{Success,Failure}

object Learning5_BlockFutures {
  def main(args: Array[String]): Unit = {

    val executors : ExecutorService = Executors.newFixedThreadPool(4)
    given executionContext : ExecutionContext = ExecutionContext.fromExecutorService(executors)

    case class User(name: String)
    case class Transaction(sender: String, receiver: String, amount: Double, status: String)

    object BankingApp {
      // APIs
      def fetchUser(name: String) : Future[User] = Future {
        // simulate some DB fetching
        Thread.sleep(500)
        User(name)
      }

      def createTransaction(user: User, merchantName: String, amount: Double) : Future[Transaction] = Future {
        Thread.sleep(1000)
        Transaction(user.name, merchantName, amount, "Success")
      }

      def purchase(username: String, item: String, merchantName: String, price:Double) : String = {
        /*
          1. Fetch user
          2. Create transaction
          3. Wait for transaction to finish
        */
        val transactionFuture = for {
          user <- fetchUser(username)
          transaction <- createTransaction(user, merchantName, price)
        } yield transaction.status
        
        // blocking call
        Await.result(transactionFuture, 5.seconds)  // extension methods
        // throws TimeoutException if the future doesn't finish within the specified time

      }
    }

    println("purchasing")
    println(BankingApp.purchase("Dharani", "Shoe", "Nike", 1000.0))
    println("purchase complete")
    executors.shutdown()

  }
}
