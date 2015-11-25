package org.tamm.client;

public class Request {
	private long hostId;
	private Long checkInIntervalSeconds;
	
	public Long getCheckInIntervalSeconds() {
		return checkInIntervalSeconds;
	}

	public void setCheckInIntervalSeconds(Long checkInIntervalSeconds) {
		this.checkInIntervalSeconds = checkInIntervalSeconds;
	}

	public Request(){}
	
	public long getHostId() {
		return hostId;
	}
	public void setHostId(long hostId) {
		this.hostId = hostId;
	}
	
	@Override
	public String toString() {
		return "Request [hostId=" + hostId + ", interval=" + checkInIntervalSeconds + "]";
	}
}
