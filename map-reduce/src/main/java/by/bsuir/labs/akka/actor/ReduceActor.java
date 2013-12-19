package by.bsuir.labs.akka.actor;

import java.util.HashMap;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import by.bsuir.labs.akka.message.MapData;
import by.bsuir.labs.akka.message.ReduceData;
import by.bsuir.labs.akka.message.WordCount;

public class ReduceActor extends UntypedActor {

	private ActorRef aggregateActor = null;

	public ReduceActor(ActorRef aggregateActorRef) {
		aggregateActor = aggregateActorRef;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof MapData) {
			MapData mapData = (MapData) message;
			ReduceData reduceData = reduce(mapData.getDataList());
			aggregateActor.tell(reduceData);
		} else
			unhandled(message);
	}

	private ReduceData reduce(List<WordCount> dataList) {
		HashMap<String, Integer> reducedMap = new HashMap<String, Integer>();
		for (WordCount wordCount : dataList) {
			if (reducedMap.containsKey(wordCount.getWord())) {
				Integer value = reducedMap.get(wordCount.getWord());
				value++;
				reducedMap.put(wordCount.getWord(), value);
			} else {
				reducedMap.put(wordCount.getWord(), 1);
			}
		}
		return new ReduceData(reducedMap);
	}
}
