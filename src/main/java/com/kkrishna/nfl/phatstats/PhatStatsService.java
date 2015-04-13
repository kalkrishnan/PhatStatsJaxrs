package com.kkrishna.nfl.phatstats;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

		return Response.status(200).entity(output).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/Teams")
	public Response getTeams() throws ClientProtocolException, IOException {

		String output = client.getResponse(resources.getTeamUrl());

		return Response.status(200).entity(output).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/Latest")
	public Response getLatestNews() throws ClientProtocolException, IOException {

		String output = newsFactory.getLatestNews(client.getResponse(resources
				.getNewsUrl()));

		return Response.status(200).entity(output).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/LatestPlayer/{playerName}")
	public Response getLatestPlayerNews(
			@PathParam("playerName") String playerName)
			throws ClientProtocolException, IOException {

		String output = newsFactory.getLatestPlayerNews(client
				.getResponse(resources.getPlayerNewsUrl(playerName)));

		return Response.status(200).entity(output).build();

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/PlayerWordCount/{playerName}")
	public Response getPlayerWordCount(
			@PathParam("playerName") String playerName)
			throws ClientProtocolException, IOException {

		Gson gson = new GsonBuilder().create();
		String output = gson.toJson(newsFactory.getPlayerWordCount(client
				.getResponse(resources.getPlayerNewsUrl(playerName))));

		return Response.status(200).entity(output).build();

	}

}
