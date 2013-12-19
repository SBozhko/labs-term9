package by.bsuir.labs.akka.message;

import java.util.List;

public class MapData {
	private final List<WordCount> dataList;

	public List<WordCount> getDataList() {
		return dataList;
	}
	public MapData(List<WordCount> dataList) {
		this.dataList = dataList;
	}
}
