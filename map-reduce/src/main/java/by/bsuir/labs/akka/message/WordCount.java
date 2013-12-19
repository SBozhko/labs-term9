package by.bsuir.labs.akka.message;

public class WordCount {

	private final String word;
	private final Integer count;

	public WordCount(String inWord, Integer inCount) {
		word = inWord;
		count = inCount;
	}

	public String getWord() {
		return word;
	}

	public Integer getCount() {
		return count;
	}
}
