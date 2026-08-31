package com

object Playground {

  trait Mappable[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  def main(args: Array[String]): Unit = {
  }
}
