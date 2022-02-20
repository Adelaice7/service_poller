package com.rmeunier.servicepoller.rest;

import com.rmeunier.servicepoller.model.Service;
import com.rmeunier.servicepoller.repo.ServiceRepository;
import com.rmeunier.servicepoller.service.PollerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 * REST API endpoints for sending Poller requests.
 */
public class PollerController {

    @Autowired
    private PollerService pollerService;

    @Autowired
    private ServiceRepository serviceRepository;

    /**
     * Poll a specific service by ID.
     * @param serviceId ID of the Service to poll
     * @return
     */
    @GetMapping("/poll/{id}")
    public String pollServiceById(@PathVariable("id") Long serviceId) {
        Service service = serviceRepository.findById(serviceId).orElse(null);

        if (service == null) {
            return null;
        }

        return pollerService.pollService(service.getUrl());
    }
}
