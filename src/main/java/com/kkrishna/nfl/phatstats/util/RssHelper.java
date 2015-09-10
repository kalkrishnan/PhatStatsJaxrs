package com.kkrishna.nfl.phatstats.util;

public class RssHelper {

	public static String convertNewsUrlToJsonFeed(String url) {
		return "https://ajax.googleapis.com/ajax/services/feed/load?v=2.0&q="
				+ url + "&num=";
	}

	public static String convertPlayerNewsUrlToJsonFeed(String playerName) {
		return "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&rsz=8&q="
				+ playerName + "%20NFL";
	}

	public static String convertPlayerInfoUrlToJsonFeed(String playerName) {
		return "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&rsz=8&q=http://search.espn.go.com/"
				+ playerName;
	}
}
