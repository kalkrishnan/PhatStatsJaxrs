package com.kkrishna.nfl.phatstats;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkrishna.nfl.phatstats.news.NewsFactory;
import com.kkrishna.nfl.phatstats.ssdp.PhatHttpClient;
import com.kkrishna.nfl.phatstats.ssdp.Resources;

@Path("Service")
public class PhatStatsService {

	@Autowired
	private Resources resources;

	@Autowired
	private PhatHttpClient client;

	@Autowired
	private NewsFactory newsFactory;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/Players")
	public Response getPlayers() throws ClientProtocolException, IOException {

		String output = client.getResponse(resources.getPlayerUrl());

		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(output).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/Teams")
	public Response getTeams() throws ClientProtocolException, IOException {

		String output = client.getResponse(resources.getTeamUrl());

		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(output).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/Latest")
	public Response getLatestNews() throws ClientProtocolException, IOException {

		String output = "[{\"topic\":\"news\",\"items\":"
				+ newsFactory.getLatestNews(client.getResponse(resources.getNewsUrl())) + "}]";

		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(output).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/LatestPlayer")
	@Encoded
	public Response getLatestPlayerNews(@QueryParam("playerName") String playerName)
			throws ClientProtocolException, IOException {

		String output = "[{\"topic\":\"news\",\"items\":"
				+ newsFactory.getLatestPlayerNews(client.getResponse(resources.getPlayerNewsUrl(playerName))) + "}]";

		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(output).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/PlayerWordCount")
	@Encoded
	public Response getPlayerWordCount(@QueryParam("playerName") String playerName)
			throws ClientProtocolException, IOException {

		Gson gson = new GsonBuilder().create();
		String output = gson
				.toJson(newsFactory.getPlayerWordCount(client.getResponse(resources.getPlayerNewsUrl(playerName))));
		System.out.println(output);
		System.out.println("Before returning count3");

		return Response.status(200).entity(output).header("Access-Control-Allow-Origin", "*").build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/PlayerInfo")
	@Encoded
	public Response getPlayerInfo(@QueryParam("playerName") String playerName)
			throws ClientProtocolException, IOException {

		Gson gson = new GsonBuilder().create();
		String output = gson.toJson(newsFactory.getPlayerInfo(resources.getPlayerInfoUrl(playerName)));
		System.out.println(output);
		System.out.println("Before returning count3");

		return Response.status(200).entity(output).header("Access-Control-Allow-Origin", "*").build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/teamPassingSchedule")
	@Encoded
	public Response getTeamPassingSchedule(@QueryParam("team") String team)
			throws ClientProtocolException, IOException {

		Gson gson = new GsonBuilder().create();
		return Response.status(200).entity(gson.toJson(getTeamDefenseSchedule(team, "Passing")))
				.header("Access-Control-Allow-Origin", "*").build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/teamRushingSchedule")
	@Encoded
	public Response getTeamRushingSchedule(@QueryParam("team") String team)
			throws ClientProtocolException, IOException {

		Gson gson = new GsonBuilder().create();
		return Response.status(200).entity(gson.toJson(getTeamDefenseSchedule(team, "Rushing")))
				.header("Access-Control-Allow-Origin", "*").build();

	}

	private Map<String, Integer> getTeamDefenseSchedule(String team, String type)
			throws ClientProtocolException, IOException {

		Map<String, Integer> schedule = new HashMap<String, Integer>();
		String entireSchedule = client.getResponse(resources.getTeamScheduleUrl(team));
		JsonElement jelement = new JsonParser().parse(entireSchedule);

		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jSchedule = jobject.getAsJsonArray("Schedule");
		for (JsonElement jsonElement : jSchedule) {
			jobject = jsonElement.getAsJsonObject();
			System.out.println(jsonElement.toString());
			String homeTeam = jobject.get("homeTeam").toString().replace("\"", "");
			String awayTeam = jobject.get("awayTeam").toString().replace("\"", "");

			String scheduleTeam = (homeTeam.equalsIgnoreCase(team)) ? awayTeam
					: ((awayTeam.equalsIgnoreCase(team) ? homeTeam : null));
			if (null != scheduleTeam) {
				int rank = Integer.parseInt(getTeamDefenseRank(scheduleTeam, type));
				schedule.put(scheduleTeam, rank);
			}

		}
		return schedule;

	}

	private String getTeamDefenseRank(String scheduleTeam, String type) {
		try {
			HashMap<String, String> defenseRanks = new LinkedHashMap<String, String>();
			Document doc = type.equals("Passing") ? Jsoup.connect(resources.getDefenseRushingRankings()).get()
					: Jsoup.connect(resources.getDefensePassingRankings()).get();
			Elements playerInfoTable = doc.select("div#my-teams-table div div table");
			Elements teams = playerInfoTable.get(0).select("tr");
			IntStream.range(0, teams.size()).parallel().forEach(i -> {
				defenseRanks.put(teams.get(i).select("a").text().trim(), teams.get(i).select("td").get(0).text());
			});
			return defenseRanks.get(Resources.normalizeTeamName(scheduleTeam));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
