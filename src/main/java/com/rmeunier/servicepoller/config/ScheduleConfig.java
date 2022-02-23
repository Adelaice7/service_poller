package com.rmeunier.servicepoller.config;

import com.rmeunier.servicepoller.service.PollerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    private PollerService pollerService;

    /**
     * Schedules to poll all services every minute.
     */
    @Async
//    @Scheduled(fixedDelay = 60000)
    public void schedulePoller() {
        pollerService.pollAllServicesAndSave();
    }
}
