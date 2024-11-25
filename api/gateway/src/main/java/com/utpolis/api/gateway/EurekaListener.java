package com.utpolis.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaListener {

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaServerUrl;

    private final Logger logger = LoggerFactory.getLogger(EurekaListener.class.getName());

    @EventListener
    public void test(ApplicationReadyEvent event) {
        logger.info("ApplicationReadyEvent: {}", event);
        logger.info("eurekaServerUrl: {}", eurekaServerUrl);
    }
}
