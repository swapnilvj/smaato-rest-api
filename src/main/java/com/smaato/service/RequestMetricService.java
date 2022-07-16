package com.smaato.service;

public interface RequestMetricService {
    void increaseRequestCount(String time, String id);

    Integer getRequestCountForId(String time, String id);

}
