package com.kkrishna.nfl.phatstats.util;

public enum Team {
	
BUF("Buffalo"),
NE("New England"),
NYJ("NY Jets"),
MIA("Miami"),
TEN("Tennessee"),
DET("Detroit"),	
DEN("Denver"),	
SEA("Seattle"),	
BAL("Baltimore"),	
PIT("Pittsburgh"),	
SF("San Francisco"),
DAL("Dallas"),	
HOU("Houston"),	
WAS("Washington"),	
ARI("Arizona"),	
STL("St. Louis"),	
PHI("Philadelphia"),
CAR("Carolina"),	
CHI("Chicago"),	
IND("Indianapolis"),
TB("Tampa Bay"),	
CIN("Cincinnati"),	
ATL("Atlanta"),	
OAK("Oakland"),	
GB("Green Bay"),	
MIN("Minnesota"),	
SD("San Diego"),	
JAC("Jacksonville"),
KC("Kansas City"),	
NO("New Orleans"),	
NYG("NY Giants"),	
CLE("Cleveland");
	
private final String normalizedName;

	Team(String normalizedName)
	{
		this.normalizedName = normalizedName;
	}
	
	public String getNormalizedName()
	{
		return this.normalizedName;
	}

}
