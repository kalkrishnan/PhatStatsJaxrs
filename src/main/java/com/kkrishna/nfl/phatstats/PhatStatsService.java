package com.kkrishna.nfl.phatstats;

import java.io.IOException;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
		String output = gson
				.toJson(newsFactory.getPlayerInfo(resources.getPlayerInfoUrl(playerName)));
		System.out.println(output);
		System.out.println("Before returning count3");

		return Response.status(200).entity(output).header("Access-Control-Allow-Origin", "*").build();

	}

}
