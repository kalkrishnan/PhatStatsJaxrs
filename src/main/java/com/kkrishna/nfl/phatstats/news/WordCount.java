package com.kkrishna.nfl.phatstats.news;

public class WordCount {

	private final String text;
	private final long weight;

	public WordCount(String text, Long long1) {
		this.text = text;
		this.weight = long1;
	}

	public String getText() {
		return text;
	}

	public long getWeight() {
		return weight;
	}

}
