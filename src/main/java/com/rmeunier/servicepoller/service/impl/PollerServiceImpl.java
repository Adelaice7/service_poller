package com.rmeunier.servicepoller.service.impl;

import com.rmeunier.servicepoller.service.PollerService;
import com.rmeunier.servicepoller.service.RequestService;
import com.rmeunier.servicepoller.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
/**
 * This service is responsible for the polling of all stored URLs.
 */
public class PollerServiceImpl implements PollerService {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private RequestService requestService;

    @Override
    public void pollAllServicesAndSave() {
        serviceService.getAllServices()
                .forEach(service -> {
                    String serviceStatus = pollService(service.getUrl());
                    service.setStatus(serviceStatus);
                    serviceService.updateService(service.getId(), service);
                });
    }

    @Override
    public String pollService(String urlStr) {
        HttpResponse response = requestService.requestUrL(urlStr);

        if (response != null) {
            int statusCode = response.statusCode();

            return HttpStatus.valueOf(statusCode).is2xxSuccessful() ? "OK" : "FAIL";
        } else {
            return "FAIL";
        }
    }
}
