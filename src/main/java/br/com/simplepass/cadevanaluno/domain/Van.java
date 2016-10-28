package br.com.simplepass.cadevanaluno.domain;

public class Van {
	private int vanId;
	private String name;
	private double latitude;
	private double longitude;
	private String direction;
	private String timeToArrive;
	private long lastUpdate;
	
	public Van() {
	}
	public Van(int vanId, String name, double latitude, double longitude, String direction, String timeToArrive,
			long lastUpdate) {
		this.vanId = vanId;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.direction = direction;
		this.timeToArrive = timeToArrive;
		this.lastUpdate = lastUpdate;
	}
	public int getVanId() {
		return vanId;
	}
	public void setVanId(int vanId) {
		this.vanId = vanId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getTimeToArrive() {
		return timeToArrive;
	}
	public void setTimeToArrive(String timeToArrive) {
		this.timeToArrive = timeToArrive;
	}
	public long getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	@Override
	public String toString() {
		return "Van [vanId=" + vanId + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", direction=" + direction + ", timeToArrive=" + timeToArrive + ", lastUpdate=" + lastUpdate + "]";
	}	
	
	
	
}
