package com.kkrishna.nfl.phatstats.ssdp;

import com.kkrishna.nfl.phatstats.util.RssHelper;

public class Resources {

	private final String PLAYER_LIST_URI;
	private final String TEAM_LIST_URI;
	private final String NEWS_URI;

	public Resources(String playerListUrl, String teamListUrl, String newsUrl) {

		this.PLAYER_LIST_URI = playerListUrl;
		this.TEAM_LIST_URI = teamListUrl;
		this.NEWS_URI = newsUrl;
	}

	public String getPlayerUrl() {
		return PLAYER_LIST_URI;
	}

	public String getTeamUrl() {
		return TEAM_LIST_URI;
	}

	public String getNewsUrl() {
		return RssHelper.convertNewsUrlToJsonFeed(NEWS_URI);
	}

	public String getPlayerNewsUrl(String playerName) {
		return RssHelper.convertPlayerNewsUrlToJsonFeed(playerName);
	}

	public String getPlayerInfoUrl(String playerName) {
		return "http://search.espn.go.com/" + playerName;
	}
}
