package org.tamm.client;

public class Request {
	private String hostId;
	private long interval;
	
	public Request(){}
	
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	@Override
	public String toString() {
		return "Request [hostId=" + hostId + ", interval=" + interval + "]";
	}
}
