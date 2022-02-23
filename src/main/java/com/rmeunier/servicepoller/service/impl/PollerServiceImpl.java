package com.rmeunier.servicepoller.service.impl;

import com.rmeunier.servicepoller.repo.ServiceRepository;
import com.rmeunier.servicepoller.service.PollerService;
import com.rmeunier.servicepoller.service.RequestService;
import com.rmeunier.servicepoller.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.List;

@Service
/*
 * This service is responsible for the polling of all stored URLs.
 */
public class PollerServiceImpl implements PollerService {

    private static final Logger logger = LoggerFactory.getLogger(PollerServiceImpl.class);

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private RequestService requestService;

    /**
     * Poll all services and save them to the repository.
     * @return false if the services are not found, otherwise true
     */
    @Override
    public boolean pollAllServicesAndSave() {
        List<com.rmeunier.servicepoller.model.Service> services = serviceService.getAllServices();
        if (services == null || services.size() == 0) {
            logger.info("No services were found in the database to poll...");
            return false;
        }

        services.forEach(service -> {
                    String serviceStatus = pollService(service.getUrl());
                    service.setStatus(serviceStatus);
                    serviceService.updateService(service.getId(), service);
                });
        return true;
    }

    /**
     * Poll a given service of ID and save it to the repository.
     * @param serviceId the service ID to poll
     * @return the saved Service
     */
    @Override
    public com.rmeunier.servicepoller.model.Service pollServiceAndSave(Long serviceId) {
        com.rmeunier.servicepoller.model.Service service = serviceService.getServiceById(serviceId);

        if (service == null) {
            return null;
        }

        String status = pollService(service.getUrl());
        service.setStatus(status);
        return serviceRepository.save(service);
    }

    /**
     * Poll a given URL.
     * @param urlStr the URL to poll
     * @return the response status of the service
     */
    @Override
    public String pollService(String urlStr) {
        HttpResponse response = requestService.requestUrL(urlStr);

        if (response != null) {
            int statusCode = response.statusCode();

            return HttpStatus.valueOf(statusCode).is2xxSuccessful() ? "OK" : "FAIL (" + HttpStatus.valueOf(statusCode) + ")";
        } else {
            return "FAIL";
        }
    }
}
