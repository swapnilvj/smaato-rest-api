package com.smaato.service.impl;


import com.smaato.service.RequestMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.smaato.util.RestServiceConstants.COUNT_OF_UNIQUE_REQUESTS_IN_CURRENT_MINUTE_MSG;
import static com.smaato.util.RestServiceConstants.DATE_FORMAT;
import static com.smaato.util.RestServiceHelper.calculateLastMinute;

@Service
public class RequestMetricServiceImpl implements RequestMetricService {

    private final static Logger logger = LoggerFactory.getLogger(RequestMetricServiceImpl.class);

    private Map<String, Map<String, Integer>> timeMap = new ConcurrentHashMap<>();

    @Override
    public synchronized void increaseRequestCount(String time, String id) {

        Map<String, Integer> idUniqueCountMap = timeMap.get(time);
        if (idUniqueCountMap == null) {
            idUniqueCountMap = new ConcurrentHashMap<>();
        }

        Integer uniqueCount = idUniqueCountMap.get(id);
        if (uniqueCount == null) {
            uniqueCount = 1;
        } else {
            uniqueCount++;
        }
        idUniqueCountMap.put(id, uniqueCount);
        timeMap.put(time, idUniqueCountMap);
    }

    @Override
    public synchronized Integer getRequestCountForId(String time, String id) {
        return timeMap.get(time).get(id);
    }

    @Scheduled(cron = "${log.counter.cron}")
    public void logCountOfUniqueRequests() {
        logger.info(COUNT_OF_UNIQUE_REQUESTS_IN_CURRENT_MINUTE_MSG);
        timeMap.getOrDefault(calculateLastMinute(), Collections.emptyMap()).entrySet().forEach(uniqueRequestEntry ->
                logger.info(String.format("\tid: %s \tCount: %d", uniqueRequestEntry.getKey(), uniqueRequestEntry.getValue())));
    }

    @Scheduled(cron = "${counter.cleanup.cron}")
    public void cleanup() {
        logger.info("TimeMap cleanup");
        String now = DATE_FORMAT.format(new Date());
        String lastMinute = calculateLastMinute();
        if (timeMap.get(now) == null && timeMap.get(lastMinute) == null) {
            timeMap.clear();
        } else {
            Map<String, Map<String, Integer>> backupTimeMap = Collections.emptyMap();
            if(timeMap.containsKey(now)) {
                backupTimeMap.put(now, timeMap.get(now));
            }
            if(timeMap.containsKey(lastMinute)) {
                backupTimeMap.put(lastMinute, timeMap.get(lastMinute));
            }

            timeMap.clear();
            timeMap.putAll(backupTimeMap);
        }
    }

}
