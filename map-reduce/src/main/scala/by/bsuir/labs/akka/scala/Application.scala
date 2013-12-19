package by.bsuir.labs.akka.scala

import java.util.ArrayList
import java.util.HashMap

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import by.bsuir.labs.akka.scala.actors.MasterActor

class Word(val word: String, val count: Integer)

case class Result

class MapData(val dataList: ArrayList[Word])

class ReduceData(val reduceDataMap: HashMap[String, java.lang.Integer])


object Application {

  def main(args: Array[String]) {
    val _system = ActorSystem("MapReduceApp")
    val master = _system.actorOf(Props[MasterActor], name = "master")

    master ! "The most important change is that an actor system can only join a cluster once"
    master ! "The most important change is that an actor system can only join a cluster once"
    master ! "The most important change"

    Thread.sleep(500)
    master ! new Result

    Thread.sleep(500)
    _system.shutdown
  }
}