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
import org.springframework.web.client.RestTemplate;

public class Client {
	private static final Logger LOG = LoggerFactory.getLogger(Client.class);
	private static final String CONF_NAME = "conf.properties";
	private static final String URL = "http://localhost:8080/hostservice"; 
	
    public static void main(String args[]) throws Exception {
    	BasicConfigurator.configure();
    	new Client().run();
    }

    public void run() throws Exception {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	Request request = new Request();
    	
    	Map<String,String> conf = getConf();
    	request.setHostId(getValue(conf,"hostId"));
    	request.setInterval(Long.parseLong(getValue(conf,"interval")));
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Authorization", getValue(conf,"auth"));
    	HttpEntity<Request> entity = new HttpEntity<Request>(request, headers);
    	
    	while(true)
    	{
    		try
    		{
    			ResponseEntity<Request> response = restTemplate.exchange(URL, HttpMethod.POST, entity, Request.class);
    			LOG.info(response.toString());
    		}
    		catch(Exception e)
    		{
    			LOG.error("Exchange exception", e);
    			e.printStackTrace();
    		}
    		Thread.sleep(request.getInterval());
    	}
    }
    
    private String getValue(Map<String,String> conf, String key) throws IllegalStateException
    {
    	String value = conf.get(key);
    	if(value == null)
    	{
    		IllegalStateException e = new IllegalStateException("Missing value in config for key \""+key+"\"");
    		LOG.error("Missing value in config for key \""+key+"\"", e);
    		throw e;
    	}
    	return value;
    }
    
	private Map<String, String> getConf() {
		Properties prop = new Properties();
		InputStream input = null;
		Map<String,String> confValues = new HashMap<String,String>();

		ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource(CONF_NAME).getFile());
		try {
			input = new FileInputStream(file);
			prop.load(input);

			confValues.put("auth",prop.getProperty("auth"));
			confValues.put("interval",prop.getProperty("interval"));
			confValues.put("hostId",prop.getProperty("hostId"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOG.error("IOException", e);
					e.printStackTrace();
				}
			}
		}
		return confValues;
	}
    
}
