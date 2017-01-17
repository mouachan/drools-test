package com.amadeus.droolsfeature.rules;

import java.util.HashMap;

public class BzrMarketDeclare {
	
	    private String region;
	    private String airport;
	    private String state;
	    private String world;
	    private String city;
	    private String country;
	    private HashMap<String, Object>matchMap;
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getAirport() {
			return airport;
		}
		public void setAirport(String airport) {
			this.airport = airport;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getWorld() {
			return world;
		}
		public void setWorld(String world) {
			this.world = world;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public HashMap<String, Object> getMatchMap() {
			return matchMap;
		}
		public void setMatchMap(HashMap<String, Object> matchMap) {
			this.matchMap = matchMap;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("BzrMarketDeclare [region=");
			builder.append(region);
			builder.append(", airport=");
			builder.append(airport);
			builder.append(", state=");
			builder.append(state);
			builder.append(", world=");
			builder.append(world);
			builder.append(", city=");
			builder.append(city);
			builder.append(", country=");
			builder.append(country);
			builder.append(", matchMap=");
			builder.append(matchMap);
			builder.append("]");
			return builder.toString();
		}

}
