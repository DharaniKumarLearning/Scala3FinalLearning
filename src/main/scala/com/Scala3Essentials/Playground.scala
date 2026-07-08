package com.Scala3Essentials

case class Customer(name: String)
case class Address(city: String)
case class Payment(cardLast4: String)

val customers = Map(
  "ord1" -> Customer("Dharani"),
  "ord2" -> Customer("Alice")
)

val addresses = Map(
  "ord1" -> Address("Chennai"),
  "ord3" -> Address("Mumbai")
)

val payments = Map(
  "ord1" -> Payment("4242"),
  "ord2" -> Payment("1234")
)

case class OrderConfirmation(customerName: String, city: String, cardLast4: String)

def confirmOrder(orderId: String): Option[OrderConfirmation] = {
  for {
    customer <- customers.get(orderId)
    address <- addresses.get(orderId)
    payment <- payments.get(orderId)
  } yield OrderConfirmation(customer.name, address.city, payment.cardLast4)
}

def confirmOrderFlatMap(orderId: String): Option[OrderConfirmation] = {
  customers.get(orderId).flatMap(customer =>
    addresses.get(orderId).flatMap(address =>
      payments.get(orderId).map(payment => OrderConfirmation(customer.name, address.city, payment.cardLast4))))
}

def describeOrder(orderId: String): String = {
  confirmOrder(orderId) match {
    case Some(orderData : OrderConfirmation) =>
      s"${orderData.customerName}'s order ships to ${orderData.city}, paid with card ending ${orderData.cardLast4}"
    case None => s"Order $orderId is incomplete"
  }
}


object Playground {
  def main(args: Array[String]): Unit = {

    println(confirmOrder("ord1"))
    println(confirmOrder("ord2"))
    println(confirmOrder("ord3"))

    println(confirmOrderFlatMap("ord1"))
    println(confirmOrderFlatMap("ord2"))
    println(confirmOrderFlatMap("ord3"))

    println(describeOrder("ord1"))
    println(describeOrder("ord2"))
    println(describeOrder("ord3"))

  }
}
