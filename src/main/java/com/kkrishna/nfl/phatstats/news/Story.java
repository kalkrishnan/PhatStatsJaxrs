package com.kkrishna.nfl.phatstats.news;

import com.google.gson.Gson;

public class Story {

	private final String headline;
	private final String description;
	private final String author;
	private final String imageUrl;

	public Story(Builder builder) {

		this.headline = builder.headline;
		this.description = builder.description;
		this.author = builder.author;
		this.imageUrl = builder.imageUrl;
	}

	public static class Builder {
		private String headline;
		private String description;
		private String author;
		private String imageUrl;

		public Builder headline(String headline) {
			this.headline = headline;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder author(String author) {
			this.author = author;
			return this;
		}

		public Builder imageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		public Story build() {
			Story news = new Story(this);
			return news;
		}
	}

	public String getHeadline() {
		return headline;
	}

	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return author;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	@Override
	public String toString() {

		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
