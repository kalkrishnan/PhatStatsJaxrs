package com.kkrishna.nfl.phatstats.news;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkrishna.nfl.phatstats.ssdp.PhatHttpClient;
import com.kkrishna.nfl.phatstats.util.ValueComparableMap;

public class NewsFactory {

	@Autowired
	private PhatHttpClient client;

	public NewsFactory() {
	}

	public String getLatestNews(String stories) throws ClientProtocolException,
			IOException {

		List<Story> news = new ArrayList<Story>();
		JsonElement jelement = new JsonParser().parse(stories);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonObject("responseData")
				.getAsJsonObject("feed").getAsJsonArray("entries");

		for (JsonElement jsonElement : jarray) {

			jobject = jsonElement.getAsJsonObject();
			String title = jobject.get("title").toString();
			String description = jobject.get("content").toString();
			String author = jobject.get("author").toString();
			String imageUrl = "";
			Story story = new Story.Builder().author(author)
					.description(description).headline(title)
					.imageUrl(imageUrl).build();
			news.add(story);

		}
		return new Gson().toJson(news);
	}

	public String getLatestPlayerNews(String response) {
		List<Story> news = new ArrayList<Story>();
		System.out.println(response);
		JsonElement jelement = new JsonParser().parse(response);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonObject("responseData")
				.getAsJsonArray("results");

		for (JsonElement jsonElement : jarray) {

			jobject = jsonElement.getAsJsonObject();
			String title = jobject.get("title").toString();
			String description = jobject.get("content").toString();
			String url = jobject.get("unescapedUrl").toString();
			if (url.contains("com/"))
				url = url.substring(0, url.indexOf("com/") + 3);
			else if (url.contains("ca/"))
				url = url.substring(0, url.indexOf("ca/") + 3);
			String imageUrl = "";
			Story story = new Story.Builder().author(url)
					.description(description).headline(title)
					.imageUrl(imageUrl).build();
			news.add(story);

		}
		return new Gson().toJson(news);
	}

	public TreeMap<String, Integer> getPlayerWordCount(String response) throws IOException {

		JsonElement jelement = new JsonParser().parse(response);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonObject("responseData")
				.getAsJsonArray("results");
		HashMap<String, Integer> words = new HashMap<String, Integer>();
		TreeMap<String, Integer> map = new ValueComparableMap<String, Integer>(
				Ordering.natural().reverse());
		for (JsonElement jsonElement : jarray) {

			jobject = jsonElement.getAsJsonObject();
			String url = jobject.get("unescapedUrl").toString();
			Document doc = Jsoup.connect(url.replaceAll("^\"|\"$", "")).get();
			System.out.println(url);
			doc.select("script, style, .hidden").remove();

			Elements newsHeadlines = doc.select("p");
			for (Element element : newsHeadlines) {
				BreakIterator boundary = BreakIterator.getWordInstance();
				boundary.setText(element.text());
				int start = boundary.first();
				for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
						.next()) {
					String word = element.text().substring(start, end);
					word = word.replaceAll("\\s+", "");
					if (word.length() > 4) {
						if (words.containsKey(word))
							words.put(word, words.get(word) + 1);
						else
							words.put(word, 1);

					}
				}
			}
			
		}
		for (String key : words.keySet()) {
			map.putAll(words);
		}
		printMap(map);
		return ((ValueComparableMap<String, Integer>) map).putFirstEntries(50);
	}

	private void printMap(TreeMap<String, Integer> map) {
		Iterator iterator = map.keySet().iterator();

		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String value = map.get(key).toString();

			System.out.println(key + " " + value);
		}
	}
}
