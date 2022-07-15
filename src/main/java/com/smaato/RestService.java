package com.smaato;

public interface RestService {
    void getCall(String id, String endpoint) throws RestServiceException;
}
