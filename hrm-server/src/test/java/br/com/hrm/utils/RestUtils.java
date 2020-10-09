package br.com.hrm.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hrm.dto.DTOBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@TestComponent
public class RestUtils {

	@Inject
	private TestRestTemplate testRestTemplate;

	private int port;
	private final String url = "http://localhost:";
	private final String bar = "/";

	public HttpHeaders getHttpHeaders(boolean auth) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(auth) {
			headers.setBasicAuth("admin", "admin");
		}

		return headers;
	}

	public String dtoToJson(DTOBase dto) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper.writeValueAsString(dto);
	}

	public HttpEntity<String> getHttpEntity(DTOBase dto, boolean auth) throws JsonProcessingException {

		return new HttpEntity<>(dtoToJson(dto), getHttpHeaders(auth));
	}

	public ResponseEntity<String> post(String uri, DTOBase dto, boolean auth) throws JsonProcessingException {
		return testRestTemplate.postForEntity(url + port + bar + uri, getHttpEntity(dto, auth), String.class);
	}

	public <T> ResponseEntity<Collection<T>> getAll(String uri, boolean auth, final Class<T> clazz) throws JsonProcessingException {

		return testRestTemplate.exchange(url + port + bar + uri, HttpMethod.GET,
				getHttpEntity(null, auth), new ParameterizedTypeReference<Collection<T>>() {
					public Type getType() {
						return new ParametrizedTypeUtil((ParameterizedType) super.getType(), new Type[] {clazz});
					}
				});
	}

	public ResponseEntity<String> put(String uri, DTOBase dto, boolean auth) throws JsonProcessingException {
		String urlPut = url + port + bar + uri;

		return testRestTemplate.exchange(urlPut, HttpMethod.PUT, getHttpEntity(dto, auth), String.class);
	}

	public ResponseEntity<String> getById(String uri, Long id, boolean auth) throws JsonProcessingException {
		String urlGet = url + port + bar + uri + "/{id}";
		Map<String, String> urlParams = new HashMap<>();
		urlParams.put("id", id.toString());
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlGet);

		return testRestTemplate.exchange(builder.buildAndExpand(urlParams).toUri(), HttpMethod.GET,
				getHttpEntity(null, auth), String.class);
	}

	public ResponseEntity<String> deleteById(String uri, Long id, boolean auth) throws JsonProcessingException {
		String urlDelete = url + port + bar + uri + "/{id}";
		Map<String, String> urlParams = new HashMap<>();
		urlParams.put("id", id.toString());
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlDelete);

		return testRestTemplate.exchange(builder.buildAndExpand(urlParams).toUri(), HttpMethod.DELETE,
				getHttpEntity(null, auth), String.class);
	}

	public <T> T convert(String content, Class<T> clazz) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper.readValue(content, clazz);
	}

	public int getPort() {

		return port;
	}

	public void setPort(int port) {

		this.port = port;
	}

}
