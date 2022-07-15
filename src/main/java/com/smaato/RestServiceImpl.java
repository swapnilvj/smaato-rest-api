package com.smaato;

import com.sun.deploy.net.HttpResponse;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.beans.Beans;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestServiceImpl implements RestService {

    @Override
    public void getCall(String id, String endpoint) throws RestServiceException {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam("endpoint", "{countOfUniqueIds}")
                .encode()
                .toUriString();

        Map<String, Integer> params = new HashMap<>();
        //TODO Get count
        params.put("countOfUniqueIds", 10);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpEntity<Object> response = restTemplate.exchange(
                    urlTemplate,
                    HttpMethod.GET,
                    entity,
                    Object.class,
                    params
            );
            //TODO Log HttpStatus
            
        } catch (RestClientException restClientException) {
            //TODO Log error
            System.out.println(restClientException.getMessage());
            //TODO Throw application exception
            throw new RestServiceException("Invalid endpoint provided.");
        } catch (Exception ex) {
            //TODO Log error
            System.out.println(ex.getMessage());
            throw ex;
        }
    }
}
