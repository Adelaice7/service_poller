package com.rmeunier.servicepoller.service.impl;

import com.rmeunier.servicepoller.service.PollerService;
import com.rmeunier.servicepoller.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
/**
 * This service is responsible for the polling of all stored URLs.
 */
public class PollerServiceImpl implements PollerService {

    @Autowired
    private ServiceService serviceService;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

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
        String serviceStatus = "";

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(urlStr))
                .build();

        HttpResponse response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            for (HttpStatus status : HttpStatus.values()) {
                if (statusCode == status.value()) {
                    serviceStatus = status.toString();
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("An error occurred polling the service!");
        }

        return serviceStatus;
    }
}
