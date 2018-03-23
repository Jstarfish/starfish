package priv.starfish.common.model;

import java.util.HashMap;
import java.util.Map;

public class GeoPoint {
	public double lat;
	public double lon;

	public GeoPoint(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public GeoPoint() {
		//
	}

	public String asString() {
		return this.lat + "," + this.lon;
	}

	public double[] asArray() {
		return new double[] { this.lon, this.lat };
	}

	public Map<String, Double> asMap() {
		Map<String, Double> retMap = new HashMap<String, Double>();
		retMap.put("lat", this.lat);
		retMap.put("lon", this.lon);
		return retMap;
	}

	@Override
	public String toString() {
		return "GeoPoint [lat=" + lat + ", lon=" + lon + "]";
	}

	public static GeoPoint newOne(Double lat, Double lon) {
		return new GeoPoint(lat, lon);
	}
}
