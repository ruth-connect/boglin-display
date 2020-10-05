package uk.me.ruthmills.boglindisplay.service.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import uk.me.ruthmills.boglindisplay.service.HomeAssistantQueryService;

@Service
public class HomeAssistantQueryServiceImpl implements HomeAssistantQueryService {

	@Value("${endpoint}")
	private String endpoint;

	@Value("${token}")
	private String token;

	private RestTemplate restTemplate;
	private final Logger logger = LoggerFactory.getLogger(HomeAssistantQueryServiceImpl.class);

	@PostConstruct
	public void initialise() {
		restTemplate = new RestTemplate(getClientHttpRequestFactory());
	}

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 9000;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
				.setSocketTimeout(timeout).build();
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		return new HttpComponentsClientHttpRequestFactory(client);
	}

	public String querySensor(String sensor) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + token);

		String sensorEndpoint = endpoint + "/" + sensor;

		logger.info("About to send GET to " + sensorEndpoint);
		ResponseEntity<String> response = restTemplate.exchange(sensorEndpoint, HttpMethod.GET,
				new HttpEntity<String>("", headers), String.class);

		String jsonString = response.getBody();
		logger.info("JSON response: " + jsonString);

		JsonParser jsonParser = JsonParserFactory.getJsonParser();
		Map<String, Object> jsonMap = jsonParser.parseMap(jsonString);

		String state = jsonMap.get("state").toString();
		logger.info("Sensor state: " + state);

		return state;
	}
}
