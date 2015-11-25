package org.tamm.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class Client {
	private static final Logger LOG = LoggerFactory.getLogger(Client.class);
	private static final String CONF_NAME = "./conf.properties";
	
    public static void main(String args[]) throws Exception {
    	BasicConfigurator.configure();
    	new Client().run();
    }

    public void run() {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	Request request = new Request();
    	String url = "";
    	
    	Map<String,String> conf = getConf();
    	//get host id
    	if(conf.get("hostId") == null)
    	{
    		throw new IllegalStateException("Missing value in config for key hostId");
    	}
    	else
    	{
    		request.setHostId(Long.parseLong(conf.get("hostId")));
    	}
    	//get host url
    	if(conf.get("url") == null)
    	{
    		throw new IllegalStateException("Missing value in config for key url");
    	}
    	else
    	{
    		url = conf.get("url");
    	}
    	//set interval
    	if(conf.get("checkInIntervalSeconds") == null)
    	{
    		//request.setCheckInIntervalSeconds(null);
    	}
    	else
    	{
    		request.setCheckInIntervalSeconds(Long.parseLong(conf.get("checkInIntervalSeconds")));
    	}
    	
    	//set authorization header
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Authorization", conf.get("auth"));

    	HttpEntity<Request> entity = new HttpEntity<Request>(request, headers);
    	
    	while(true)
    	{
    		try
    		{
    			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    			LOG.info(response.getBody());
    		}
    		catch(HttpStatusCodeException e)
    		{
    			LOG.error(e.getResponseBodyAsString());
    			LOG.error("Exception while sending request", e);
    		}
    		catch(RestClientException e)
    		{
    			LOG.error("Exception while sending request", e);
    		}
    		
    		try {
    			//TODO: get interval from response body
    			if(request.getCheckInIntervalSeconds() == null)
    			{
    				request.setCheckInIntervalSeconds(new Long(4000));
    			}
				Thread.sleep(request.getCheckInIntervalSeconds());
			} catch (InterruptedException e) {
				LOG.error("Sleep interrupted",e);
			}
    	}
    }
    
	private Map<String, String> getConf() {
		Properties prop = new Properties();
		InputStream input = null;
		Map<String,String> confValues = new HashMap<String,String>();

		File file = new File(CONF_NAME);
		
		try {
			input = new FileInputStream(file);
			prop.load(input);

			confValues.put("auth",prop.getProperty("auth"));
			confValues.put("checkInIntervalSeconds",prop.getProperty("checkInIntervalSeconds"));
			confValues.put("hostId",prop.getProperty("hostId"));
			confValues.put("url",prop.getProperty("url"));
		} catch (IOException ex) {
			LOG.error("Error reading configuration", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOG.error("Error reading configuration", e);
				}
			}
		}
		return confValues;
	}
    
}
