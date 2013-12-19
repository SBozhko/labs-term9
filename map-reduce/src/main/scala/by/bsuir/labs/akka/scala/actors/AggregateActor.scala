package by.bsuir.labs.akka.scala.actors

import java.util.HashMap

import scala.collection.JavaConversions.asScalaSet

import akka.actor.Actor
import by.bsuir.labs.akka.scala.{ReduceData, Result}

class AggregateActor extends Actor {

  var finalReducedMap = new HashMap[String, java.lang.Integer]

  def receive: Receive = {
    case message: ReduceData =>
      aggregateInMemoryReduce(message.reduceDataMap)
    case message: Result =>
      System.out.println(finalReducedMap.toString)
  }

  def aggregateInMemoryReduce(reducedList: HashMap[String, java.lang.Integer]) {
    var count: java.lang.Integer = 0
    for (key <- reducedList.keySet) {
      if (finalReducedMap.containsKey(key)) {
        count = reducedList.get(key)
        count += finalReducedMap.get(key)
        finalReducedMap.put(key, count)
      } else {
        finalReducedMap.put(key, reducedList.get(key))
      }
    }
  }
}