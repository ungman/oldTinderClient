package io.github.ungman.client.utils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

@Component
public class RestClientHelper {
    @Value("${server.uri}")
    private String server;
    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatus status;

    public RestClientHelper() {
        this.rest = new RestTemplate();

        this.headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Transfer-Encoding", "chunked");
        headers.set("Date", LocalDate.now().toString());
        headers.set("Keep-Alive", "timeout=60");
        headers.set("Connection", "keep-alive");
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String get(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        String uri1=server + uri;
        ResponseEntity<String> responseEntity = rest.exchange(uri1, HttpMethod.GET, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public String post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public String put(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.PUT, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    public void delete(String uri) {
        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.DELETE, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }


    @SneakyThrows
    public boolean checkAvailableConnection(String uri) {
        URL url = new URL(server  + uri);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        boolean result;
        try {
            int responseCode = huc.getResponseCode();
            result = responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception ignored) {
            result = false;
        }
        return result;
    }
}