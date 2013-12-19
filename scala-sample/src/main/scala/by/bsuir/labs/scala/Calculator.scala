package by.bsuir.labs.scala

import collection.mutable.ArrayBuffer

class Calculator() {
  val numbers = new ArrayBuffer[Double]()

  def add(number: Double): Unit = {
    numbers += number
  }

  def avg: Double = {
    var sum: Double = 0.0
    for (x: Double <- numbers) {
      sum = sum + x
    }
    sum / numbers.length
  }

  def avg_short: Double = numbers.reduceLeft(_ + _) / numbers.length

  def reset: Unit = {
    numbers.clear()
  }

}

object SimpleStatistics {

  def main(args: Array[String]) {
    val c = new Calculator()
    for (i <- 1 to 10) c.add(i)
    println(c.avg_short)
  }

}

