package com.rmeunier.servicepoller.rest;

import com.rmeunier.servicepoller.model.Service;
import com.rmeunier.servicepoller.repo.ServiceRepository;
import com.rmeunier.servicepoller.service.PollerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/*
 * REST API endpoints for sending Poller requests.
 */
public class PollerController {

    @Autowired
    private PollerService pollerService;

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/poll/{id}")
    public ResponseEntity<String> pollServiceById(@PathVariable("id") Long serviceId) {
        Service service = serviceRepository.findById(serviceId).orElse(null);

        if (service == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }

        String serviceStatus = pollerService.pollService(service.getUrl());

        return ResponseEntity.accepted().body(serviceStatus);
    }

    @PostMapping("/poll/{id}")
    public ResponseEntity<Service> pollServiceAndSave(@PathVariable("id") Long serviceId) {
        Service service = pollerService.pollServiceAndSave(serviceId);

        if (service == null) {
            return ResponseEntity.unprocessableEntity().body(null);
        }
        return ResponseEntity.accepted().body(service);
    }

    @GetMapping("/pollServices")
    public boolean pollAllServices() {
        return pollerService.pollAllServicesAndSave();
    }
}
