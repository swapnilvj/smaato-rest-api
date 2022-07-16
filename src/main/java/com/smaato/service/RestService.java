package com.smaato.service;

import com.smaato.exception.RestServiceException;

public interface RestService {
    void getCall(String id, String endpoint) throws RestServiceException;
}
