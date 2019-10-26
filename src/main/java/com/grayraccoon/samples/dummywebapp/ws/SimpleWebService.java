package com.grayraccoon.samples.dummywebapp.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/ws")
public class SimpleWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleWebService.class);

    @Value("${server.port}")
    private int port;

    @GetMapping("/port")
    public Map<String, String> getPort() {
        LOGGER.info("Performing GetPort.");
        final Map<String, String> response = Collections
                .singletonMap("port", String.valueOf(port));
        LOGGER.info("Responding: {}", response);
        return response;
    }

}
