package com.smaato.service;

import com.smaato.exception.RestServiceException;

public interface RestService {
    void callEndpointUrl(String id, String endpoint, String timestamp) throws RestServiceException;
}
