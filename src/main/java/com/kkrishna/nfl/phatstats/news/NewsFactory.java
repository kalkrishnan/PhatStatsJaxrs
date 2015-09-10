package com.kkrishna.nfl.phatstats.news;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger logger = LogManager.getLogger(NewsFactory.class.getName());;

	public NewsFactory() {
	}

	public String getLatestNews(String stories) throws ClientProtocolException, IOException {

		List<Story> news = new ArrayList<Story>();
		JsonElement jelement = new JsonParser().parse(stories);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonObject("responseData").getAsJsonObject("feed").getAsJsonArray("entries");

		for (JsonElement jsonElement : jarray) {

			jobject = jsonElement.getAsJsonObject();
			String title = jobject.get("title").toString();
			String description = jobject.get("content").toString();
			String author = jobject.get("author").toString();
			String imageUrl = "";
			Story story = new Story.Builder().author(author).description(description).headline(title).imageUrl(imageUrl)
					.build();
			news.add(story);

		}
		return new Gson().toJson(news);
	}

	public String getLatestPlayerNews(String response) {
		List<Story> news = new ArrayList<Story>();
		System.out.println(response);
		JsonElement jelement = new JsonParser().parse(response);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonObject("responseData").getAsJsonArray("results");

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
			Story story = new Story.Builder().author(url).description(description).headline(title).imageUrl(imageUrl)
					.build();
			news.add(story);

		}
		return new Gson().toJson(news);
	}

	public Set<WordCount> getPlayerWordCount(String response) throws IOException {

		try {
			JsonElement jelement = new JsonParser().parse(response);
			JsonObject jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonObject("responseData").getAsJsonArray("results");
			HashMap<String, Long> words = new HashMap<String, Long>();
			Elements newsHeadlines = new Elements();
			TreeMap<String, Long> map = new ValueComparableMap<String, Long>(Ordering.natural().reverse());
			Document doc = null;
			for (JsonElement jsonElement : jarray) {

				jobject = jsonElement.getAsJsonObject();
				String url = jobject.get("unescapedUrl").toString();
				try {
					doc = Jsoup.connect(url.replaceAll("^\"|\"$", "")).get();
					doc.select("script, style, .hidden").remove();

					newsHeadlines.addAll(doc.select("p"));
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
			words = (HashMap<String, Long>) newsHeadlines.stream().map(e -> NewsFactory.getElementText(e))
					.flatMap(Collection::stream).collect(Collectors.groupingBy(o -> o, Collectors.counting()));
			map.putAll(words);
			map = ((ValueComparableMap<String, Long>) map).putFirstEntries(50);
			System.out.println("Before returning count");
			System.out.println("Before returning count2" + map.toString());
			return map.entrySet().stream().map(e -> new WordCount(e.getKey(), e.getValue()))
					.collect(Collectors.<WordCount> toSet());
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public HashMap<String, String> getPlayerInfo(String url) {
		try {
			HashMap<String, String> playerInfo = new LinkedHashMap<String, String>();
			Document doc = Jsoup.connect(url).get();
			Elements playerInfoTable = doc.select("div#playerInfo div table");
			Elements stats = playerInfoTable.get(0).select("tr th");
			Elements values = playerInfoTable.get(0).select("tr td");
			IntStream.range(0, stats.size()).parallel().forEach(i -> {
				playerInfo.put(stats.get(i).text(), values.get(i).text());
			});
			Elements playerPic = doc.select("div.card-img img");
			playerInfo.put("image", playerPic.get(0).attr("src"));
			return playerInfo;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static List<String> getElementText(Element element) {
		List<String> words = new ArrayList<String>();
		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(element.text());
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
			String word = element.text().substring(start, end);
			word = word.replaceAll("\\s+", "");
			if (word.length() > 4)
				words.add(word);
		}
		return words;
	}

}
