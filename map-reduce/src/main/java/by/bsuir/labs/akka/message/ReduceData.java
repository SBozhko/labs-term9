package by.bsuir.labs.akka.message;

import java.util.HashMap;

public class ReduceData {

	private HashMap<String, Integer> reduceDataList;

	public HashMap<String, Integer> getReduceDataList() {
		return reduceDataList;
	}

	public ReduceData(HashMap<String, Integer> reduceDataList) {
		this.reduceDataList = reduceDataList;
	}
}
