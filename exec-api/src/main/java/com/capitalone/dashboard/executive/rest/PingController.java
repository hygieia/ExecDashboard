package com.capitalone.dashboard.executive.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/ping")
@CrossOrigin
public class PingController {

    public PingController() {}

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> ping () {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(true);
    }
}
