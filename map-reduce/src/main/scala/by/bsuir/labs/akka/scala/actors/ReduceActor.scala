package by.bsuir.labs.akka.scala.actors

import java.util.ArrayList
import java.util.HashMap

import scala.collection.JavaConversions.asScalaBuffer

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import by.bsuir.labs.akka.scala.{ReduceData, MapData, Word}

class ReduceActor(aggregateActor: ActorRef) extends Actor {

  def receive: Receive = {
    case message: MapData =>
      aggregateActor ! reduce(message.dataList)
    case _ =>

  }

  def reduce(dataList: ArrayList[Word]): ReduceData = {
    val reducedMap = new HashMap[String, java.lang.Integer]
    for (wc: Word <- dataList) {
      val word: String = wc.word
      if (reducedMap.containsKey(word)) {
        reducedMap.put(word, reducedMap.get(word) + 1)
      } else {
        reducedMap.put(word, 1)
      }
    }
    new ReduceData(reducedMap)
  }
}