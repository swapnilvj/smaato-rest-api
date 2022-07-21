package com.smaato.service.impl;

import com.smaato.exception.RestServiceException;
import com.smaato.service.RequestMetricService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class RestServiceImplTest {

    @InjectMocks
    RestServiceImpl restService;
    @Mock
    RequestMetricService requestMetricService;
    @Mock
    RestTemplate restTemplate;

    @Test
    public void testCallEndpointUrl() throws RestServiceException {
        String time = Calendar.getInstance().toString();
        Mockito.when(requestMetricService.getRequestCountForId(time, "11")).thenReturn(100);
        ResponseEntity responseEntity = new ResponseEntity<String>(HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(Mockito.anyString(),Mockito.any(), Mockito.anyMap())).thenReturn(responseEntity);
        restService.callEndpointUrl(
                "11","https://jsonplaceholder.typicode.com/todos/1", time);

        Map<String, Integer> params = new HashMap<>();
        params.put("count", 100);
        Mockito.verify(restTemplate).getForEntity(
                "https://jsonplaceholder.typicode.com/todos/1?countOfUniqueIds={count}", Object.class, params);
    }
}