package com.kkrishna.nfl.phatstats;

import org.glassfish.jersey.server.ResourceConfig;

public class PhatStatsApp extends ResourceConfig {

	public PhatStatsApp() {
		register(PhatStatsService.class);
	}
}
