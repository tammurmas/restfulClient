package org.tamm.client;

public class Request {
	private String uuid;
	private Long checkInIntervalSeconds;

	public Long getCheckInIntervalSeconds() {
		return checkInIntervalSeconds;
	}

	public void setCheckInIntervalSeconds(Long checkInIntervalSeconds) {
		this.checkInIntervalSeconds = checkInIntervalSeconds;
	}

	public Request() {
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Request [uuid=" + uuid + ", interval=" + checkInIntervalSeconds + "]";
	}
}
