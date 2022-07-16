package com.smaato.util;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static com.smaato.util.RestServiceConstants.DATE_FORMAT;
import static com.smaato.util.RestServiceConstants.QUERY_PARAM_ENDPOINT;

public class RestServiceHelper {
    public static String buildEndpointUrlTemplate(String endpoint) {
        return UriComponentsBuilder.fromHttpUrl(endpoint)
                .queryParam(QUERY_PARAM_ENDPOINT, "{count}")
                .encode()
                .toUriString();
    }

    public static RestTemplate buildRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    public static String calculateLastMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, -1);
        return DATE_FORMAT.format(calendar.getTime());
    }
}
