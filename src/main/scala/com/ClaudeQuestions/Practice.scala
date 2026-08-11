package com.ClaudeQuestions


object Practice {
  def main(args: Array[String]): Unit = {

    class ConsoleWriter {
      def write(s: String): Unit = println(s)
    }

    trait Timestamped extends ConsoleWriter {
      override def write(s: String): Unit = super.write(s"[ts] $s")
    }

    trait Upper extends ConsoleWriter {
      override def write(s: String): Unit = super.write(s.toUpperCase)
    }


    new ConsoleWriter with Timestamped with Upper write "hello"
    new ConsoleWriter with Upper with Timestamped write "hello"



  }
}
