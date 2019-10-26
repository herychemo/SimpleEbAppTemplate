package com.grayraccoon.samples.dummywebapp.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RootWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootWebService.class);

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @GetMapping(value = {"/", "/index"})
    public RedirectView index() {
        LOGGER.info("Request on root, redirecting to service.");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(
                contextPath.concat("/ws/port")
        );
        return redirectView;
    }

}
