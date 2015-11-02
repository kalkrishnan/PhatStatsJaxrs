package com.kkrishna.nfl.phatstats.ssdp;

import com.kkrishna.nfl.phatstats.util.RssHelper;
import com.kkrishna.nfl.phatstats.util.Team;

public class Resources {

	private final String TEAM_SCHEDULE_URL;
	private final String PLAYER_LIST_URL;
	private final String TEAM_LIST_URL;
	private final String NEWS_URL;
	private final String DEFENSE_RUSHING_RANKINGS;
	private final String DEFENSE_PASSING_RANKINGS;
	private final String OFFENSE_RANKINGS;

	public Resources(String playerListUrl, String teamListUrl, String newsUrl, String teamScheduleUrl,
			String defenseRushingRanks, String defensePassingRanks, String offenseRanks) {

		this.PLAYER_LIST_URL = playerListUrl;
		this.TEAM_LIST_URL = teamListUrl;
		this.NEWS_URL = newsUrl;
		this.TEAM_SCHEDULE_URL = teamScheduleUrl;
		this.DEFENSE_PASSING_RANKINGS = defensePassingRanks;
		this.DEFENSE_RUSHING_RANKINGS = defenseRushingRanks;
		this.OFFENSE_RANKINGS = offenseRanks;
	}

	public String getPlayerUrl() {
		return PLAYER_LIST_URL;
	}

	public String getTeamUrl() {
		return TEAM_LIST_URL;
	}

	public String getNewsUrl() {
		return RssHelper.convertNewsUrlToJsonFeed(NEWS_URL);
	}

	public String getPlayerNewsUrl(String playerName) {
		return RssHelper.convertPlayerNewsUrlToJsonFeed(playerName);
	}

	public String getPlayerInfoUrl(String playerName) {
		return "http://search.espn.go.com/" + playerName;
	}

	public String getTeamScheduleUrl(String team) {
		return TEAM_SCHEDULE_URL;
	}

	public String getDefenseRushingRankings() {
		return this.DEFENSE_RUSHING_RANKINGS;
	}

	public String getDefensePassingRankings() {
		return this.DEFENSE_PASSING_RANKINGS;
	}

	public String getOffenseRankings() {
		return this.OFFENSE_RANKINGS;
	}

	public static String normalizeTeamName(String team) {
		return Team.valueOf(team).getNormalizedName();
	}
}
