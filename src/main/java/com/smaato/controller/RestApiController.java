package com.smaato.controller;

import com.smaato.exception.RestServiceException;
import com.smaato.service.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
public class RestApiController {

    private final static Logger logger = LoggerFactory.getLogger(RestApiController.class);

    public static final String RESPONSE_OK = "ok";
    public static final String RESPONSE_FAILED = "failed";

    private final RestService restService;

    @Autowired
    public RestApiController(RestService restService) {
        this.restService = restService;
    }

    @RequestMapping("/api/smaato/accept")
    public String processRequest(@RequestParam String id,
                                 @RequestParam(required = false) String endpoint,
                                 @RequestAttribute String timestamp) {

        if (null != endpoint) {
            try {
                restService.callEndpointUrl(id, endpoint, timestamp);
            } catch (Exception | RestServiceException ex) {
                return RESPONSE_FAILED;
            }
        }
        return RESPONSE_OK;
    }

}
