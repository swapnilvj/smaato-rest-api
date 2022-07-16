package com.smaato.service.impl;

import com.smaato.exception.RestServiceException;
import com.smaato.service.RequestMetricService;
import com.smaato.service.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.smaato.util.RestServiceConstants.*;
import static com.smaato.util.RestServiceHelper.buildEndpointUrlTemplate;
import static com.smaato.util.RestServiceHelper.buildRestTemplate;

@Service
public class RestServiceImpl implements RestService {

    private final static Logger logger = LoggerFactory.getLogger(RestServiceImpl.class);

    private RequestMetricService requestMetricService;

    @Autowired
    public RestServiceImpl(RequestMetricService requestMetricService) {
        this.requestMetricService = requestMetricService;
    }

    @Override
    public void callEndpointUrl(String id, String endpoint, String timestamp) throws RestServiceException {
        String endpointUrlTemplate = buildEndpointUrlTemplate(endpoint);

        Map<String, Integer> params = buildQueryParamMap(id, timestamp);

        try {
            RestTemplate restTemplate = buildRestTemplate();

            ResponseEntity<Object> responseEntity = restTemplate.getForEntity(endpointUrlTemplate, Object.class, params);
            logger.info(String.format(ENDPOINT_RESPONSE_STATUS, responseEntity.getStatusCode()));

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            logger.info(String.format(ENDPOINT_RESPONSE_STATUS, httpClientOrServerExc.getStatusCode()));
            throw new RestServiceException(String.format(REST_SERVICE_EXCEPTION_ERROR_MSG, id, endpoint));

        } catch (Exception ex) {

            logger.info(ex.getMessage());
            logger.info(String.format(APPLICATION_COULD_NOT_PROCESS_THE_RESPONSE_ERROR_MSG, id, endpoint));
            throw ex;

        }
    }

    private Map<String, Integer> buildQueryParamMap(String id, String timestamp) {
        Map<String, Integer> params = new HashMap<>();
        Integer uniqueRequestCount = getUniqueRequestCount(id, timestamp);
        params.put("count", uniqueRequestCount);
        return params;
    }

    private Integer getUniqueRequestCount(String id, String timestamp) {
        return requestMetricService.getRequestCountForId(timestamp, id);
    }

}
