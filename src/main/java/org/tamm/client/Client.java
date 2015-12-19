package org.tamm.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class Client {
	private static final Logger LOG = LoggerFactory.getLogger(Client.class);
	private static final String CONF_NAME = "./conf.properties";
	private Properties properties = new Properties();
	Request request = new Request();
	RestTemplate restTemplate = new RestTemplate();
	URI url;

	public static void main(String args[]) throws Exception {
		new Client().run();
	}

	public void run() {
		try {
			init();
			start();
		} catch (InitializationException ie) {
			LOG.error("Could not start: {}.", ie.getMessage());
			printStack(ie);
		}
	}

	private void init() throws InitializationException {
		try {
			loadProperties();
			loadLogConf();

			checkProperty(ConfKeys.UUID);
			checkProperty(ConfKeys.URL);

			url = new URI(properties.getProperty(ConfKeys.URL));
			request.setUuid(properties.getProperty(ConfKeys.UUID));

			if (!StringUtils.isEmpty(properties.getProperty(ConfKeys.INTERVAL_SECONDS))) {
				try {
					request.setCheckInIntervalSeconds(
							Long.parseLong(properties.getProperty(ConfKeys.INTERVAL_SECONDS)));
				} catch (NumberFormatException e) {
					LOG.info(
							"Could not parse {} value from conf file, value will be taken from service response instead.",
							ConfKeys.INTERVAL_SECONDS);
					LOG.debug("", e);
				}
			}
		} catch (Exception e) {
			throw new InitializationException(e);
		}
	}

	private void start() {
		try {
			LOG.info("Started");
			HttpEntity<Request> entity = new HttpEntity<Request>(request);
			boolean showBackOnline = false;
			while (true) {
				try {
					ResponseEntity<HostServiceResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity,
							HostServiceResponse.class);
					HostServiceResponse resp = response.getBody();
					LOG.debug("Response body: " + resp);
					if (request.getCheckInIntervalSeconds() == null || request.getCheckInIntervalSeconds()
							.longValue() != resp.getCheckInIntervalSeconds().longValue()) {
						LOG.info("Server did not like my interval time ({} seconds) and wanted me to use {}.",
								request.getCheckInIntervalSeconds(), resp.getCheckInIntervalSeconds());
						request.setCheckInIntervalSeconds(resp.getCheckInIntervalSeconds());
					}
					if (showBackOnline) {
						LOG.info("Connected to server successfully");
						showBackOnline = false;
					}
				} catch (RestClientException e) {
					showBackOnline = true;
					LOG.error("Error: {}.", e.getMessage());
					printStack(e);
				}

				try {
					Thread.sleep(request.getCheckInIntervalSeconds() * 1000);
				} catch (InterruptedException e) {
					LOG.error("Sleep interrupted");
					printStack(e);
				}
			}
		} finally {
			LOG.info("Closed.");
		}
	}

	private void checkProperty(String key) {
		if (StringUtils.isEmpty(properties.getProperty(key))) {
			throw new IllegalStateException("Missing value in conf file for key: " + key);
		}
	}

	private void loadProperties() {
		File file = new File(CONF_NAME);
		try (InputStream input = new FileInputStream(file)) {
			properties.load(input);
		} catch (IOException ex) {
			LOG.error("Error reading configuration", ex);
		}
	}

	private void loadLogConf() throws IOException {
		Properties props = new Properties();
		props.load(getClass().getResourceAsStream("/log4j.properties"));
		PropertyConfigurator.configure(props);
	}

	private void printStack(Exception e) {
		if (LOG.isDebugEnabled()) {
			LOG.error("", e); // stacktrace
		}
	}
}
