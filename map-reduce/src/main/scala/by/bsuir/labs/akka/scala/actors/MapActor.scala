package by.bsuir.labs.akka.scala.actors

import java.util.ArrayList
import java.util.StringTokenizer

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import by.bsuir.labs.akka.scala.{MapData, Word}

class MapActor(reduceActor: ActorRef) extends Actor {

  val STOP_WORDS_LIST = List("a", "am", "an", "and", "are", "as", "at", "be", "do", "go", "if", "in", "is", "it", "of", "on", "the", "to")

  def receive: Receive = {
    case message: String =>
      reduceActor ! evaluateExpression(message)
    case _ =>

  }

  def evaluateExpression(line: String): MapData = {
    val dataList = new ArrayList[Word]
    val parser: StringTokenizer = new StringTokenizer(line)
    val defaultCount: Integer = 1
    while (parser.hasMoreTokens) {
      val word: String = parser.nextToken().toLowerCase
      if (!STOP_WORDS_LIST.contains(word)) {
        dataList.add(new Word(word, defaultCount))
      }
    }
    new MapData(dataList)
  }
}
