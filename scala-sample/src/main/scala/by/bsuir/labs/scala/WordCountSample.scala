package by.bsuir.labs.scala

import scala.io.Source

object WordCountSample {

  def main(args: Array[String]) {
    val lines = Source.fromFile("scala-sample\\src\\main\\resources\\in.txt").getLines.toArray
    val counts = new collection.mutable.HashMap[String, Int].withDefaultValue(0)
    lines.flatMap(line => line.split(" ")).foreach(word => counts(word) += 1)

    print(counts)
  }
}
