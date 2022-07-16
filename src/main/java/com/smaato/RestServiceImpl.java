package com.smaato;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static com.smaato.RestServiceConstants.*;

@Service
public class RestServiceImpl implements RestService {

    @Override
    public void getCall(String id, String endpoint) throws RestServiceException {
        String endpointUrlTemplate = buildEndpointUrlTemplate(endpoint);

        Map<String, Integer> params = buildQueryParamMap();

        try {
            RestTemplate restTemplate = buildRestTemplate();

            ResponseEntity<Object> responseEntity = restTemplate.getForEntity(endpointUrlTemplate, Object.class, params);
            //TODO Log HttpStatus
            System.out.println(String.format(ENDPOINT_RESPONSE_STATUS,responseEntity.getStatusCode()));


        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            //TODO Log
            System.out.println(String.format(ENDPOINT_RESPONSE_STATUS,httpClientOrServerExc.getStatusCode()));
            throw new RestServiceException(String.format(REST_SERVICE_EXCEPTION_ERROR_MSG, id, endpoint));
        } catch (Exception ex) {
            //TODO Log error
            System.out.println(ex.getMessage());
            System.out.println(String.format(APPLICATION_COULD_NOT_PROCESS_THE_RESPONSE_ERROR_MSG, id, endpoint));
            throw ex;
        }
    }

    private Map<String, Integer> buildQueryParamMap() {
        Map<String, Integer> params = new HashMap<>();
        //TODO Get count
        params.put("count", 10);
        return params;
    }

    private String buildEndpointUrlTemplate(String endpoint) {
        return UriComponentsBuilder.fromHttpUrl(endpoint)
                    .queryParam(QUERY_PARAM_ENDPOINT, "{count}")
                    .encode()
                    .toUriString();
    }

    private RestTemplate buildRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }
}
