package com.smaato;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    public static final String RESPONSE_OK = "ok";
    public static final String RESPONSE_FAILED = "failed";

    private final RestService restService;

    @Autowired
    public RestController(RestService restService) {
        this.restService = restService;
    }

    @RequestMapping("/api/smaato/accept")
    public String processRequest(@RequestParam String id,
                                                 @RequestParam(required = false) String endpoint) {
        if(null != endpoint) {
            try {
                restService.getCall(id, endpoint);
            } catch (Exception | RestServiceException ex) {
                return RESPONSE_FAILED;
            }
        }
        return RESPONSE_OK;
    }

}
