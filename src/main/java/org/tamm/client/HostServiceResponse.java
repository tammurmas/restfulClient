package org.tamm.client;

public class HostServiceResponse {

	private Long checkInIntervalSeconds;

	public Long getCheckInIntervalSeconds() {
		return checkInIntervalSeconds;
	}

	public void setCheckInIntervalSeconds(Long checkInIntervalSeconds) {
		this.checkInIntervalSeconds = checkInIntervalSeconds;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HostServiceResponse (checkInIntervalSeconds=");
		builder.append(checkInIntervalSeconds);
		builder.append(")");
		return builder.toString();
	}
}

