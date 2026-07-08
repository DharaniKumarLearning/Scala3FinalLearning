package com.Scala3Essentials.part3fp

import scala.annotation.tailrec

object Learning7_TuplesMapsExercises {

  /*
    Social Network = Map[String, Set[String]]
    Friend relationships are mutual
      - add a Person to the network
      - remove a Person from network
      - add friend relationship
      - unfriend

      - number of friends of a Person
      - who has most friends
      - how many people have no friends
  */

  private def addPerson(network : Map[String, Set[String]], newPerson : String) : Map[String, Set[String]] = network + (newPerson -> Set())
  private def removePerson(network : Map[String, Set[String]], removePerson : String) : Map[String, Set[String]] =
    (network - removePerson).map(pair => (pair._1, pair._2 - removePerson))

  private def friend(network : Map[String, Set[String]], a : String, b : String) : Map[String, Set[String]] = {
    if(!network.contains(a)) throw new IllegalArgumentException(s"The Person $a is not part of the network")
    else if(!network.contains(b)) throw new IllegalArgumentException(s"The Person $b is not part of the network")
    else {
      val friendsA = network(a)
      val friendsB = network(b)
      network + (a -> (friendsA + b)) + (b -> (friendsB + a))
    }
  }

  private def unfriend(network : Map[String, Set[String]], a : String, b : String) : Map[String, Set[String]] = {
    if(!network.contains(a) || !network.contains(b)) network
    else {
      val friendsA = network(a)
      val friendsB = network(b)
      network + (a -> (friendsA - b)) + (b -> (friendsB - a))
    }
  }

  private def nFriends(network : Map[String, Set[String]], person : String) : Int =
    if(!network.contains(person)) -1 else network(person).size

  private def mostFriends(network : Map[String, Set[String]]) : String =
    if(network.isEmpty) throw new RuntimeException("No one with most friends")
    else {
      val best = network.foldLeft(("", -1)) { (currentBest, newAssociation) =>
        val currentMostPopularPerson = currentBest._1
        val mostFriends = currentBest._2
        val newPerson = newAssociation._1
        val newPersonFriends = newAssociation._2.size
        if(mostFriends < newPersonFriends) (newPerson, newPersonFriends)
        else currentBest
      }
      best._1
    }

  private def nPeopleWithNoFriends(network : Map[String, Set[String]]) : Int =
    network.count(pair => pair._2.isEmpty)


  def main(args: Array[String]): Unit = {

    val empty : Map[String, Set[String]] = Map()
    val onePersonNetwork = addPerson(empty, "Mary")
    val twoPersonNetwork = addPerson(onePersonNetwork, "Dharani")
    val threePersonNetwork = addPerson(twoPersonNetwork, "Daniel")
    println(threePersonNetwork)
    println(nPeopleWithNoFriends(threePersonNetwork))

    val networkWithMaryDharani = friend(threePersonNetwork, "Mary", "Dharani")
    println(nPeopleWithNoFriends(networkWithMaryDharani))
    val networkWithMaryDaniel = friend(networkWithMaryDharani, "Mary", "Daniel")
    println(nPeopleWithNoFriends(networkWithMaryDaniel))
    println(networkWithMaryDaniel)
    println(nFriends(networkWithMaryDaniel, "Mary"))
    println(nFriends(networkWithMaryDaniel, "Dharani"))
    println(nFriends(networkWithMaryDaniel, "Jim"))
    println(mostFriends(networkWithMaryDaniel))

    val networkWithOutMaryDharani = unfriend(networkWithMaryDaniel, "Mary", "Dharani")
    println(networkWithOutMaryDharani)

  }
}
