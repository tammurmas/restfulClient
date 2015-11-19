package org.tamm.client;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Client {
	private static final Logger log = LoggerFactory.getLogger(Client.class);
	private long interval;
	private String host;

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public Client(long interval, String host)
	{
		this.interval = interval;
		this.host = host;
	}
	
    public static void main(String args[]) throws Exception {
    	BasicConfigurator.configure();
    	new Client(2000, "http://localhost:8080/hostservice").run();
    }

    public void run(String... strings) throws Exception {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	ServerStatus status = new ServerStatus();
    	status.setHostId("randomId");
    	status.setStatus("randomStatus");
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
    	HttpEntity<ServerStatus> entity = new HttpEntity<ServerStatus>(status, headers);
    	
    	while(true)
    	{
    		try
    		{
    			//ResponseEntity<ServerStatus> response = restTemplate.postForEntity(host, status, ServerStatus.class);
    			ResponseEntity<ServerStatus> response = restTemplate.exchange(host, HttpMethod.POST, entity, ServerStatus.class);
            	log.info(response.toString());
            	Thread.sleep(interval);
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    	}
    }
}
