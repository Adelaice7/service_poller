package com.rmeunier.servicepoller.service.impl;

import com.rmeunier.servicepoller.model.User;
import com.rmeunier.servicepoller.repo.ServiceRepository;
import com.rmeunier.servicepoller.repo.UserRepository;
import com.rmeunier.servicepoller.service.ServiceService;
import com.rmeunier.servicepoller.service.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
/**
 * This service is responsible for communicating with the repository of the Service objects.
 */
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UrlValidator urlValidator;

    @Override
    public List<com.rmeunier.servicepoller.model.Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public List<com.rmeunier.servicepoller.model.Service> getAllServicesByUserId(Long userId) {
        return serviceRepository.findByUserId(userId);
    }

    @Override
    public com.rmeunier.servicepoller.model.Service getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @Override
    public com.rmeunier.servicepoller.model.Service addService(com.rmeunier.servicepoller.model.Service service,
                                                               Long userId) {
        if (!urlValidator.isValidUrl(service.getUrl())) {
            System.err.println("Invalid URL!");
            return null;
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            service.setUser(user);
            service.setTimestamp(LocalDateTime.now());
            return serviceRepository.save(service);
        }
        return null;
    }

    @Override
    public com.rmeunier.servicepoller.model.Service updateService(Long id,
                                                                  com.rmeunier.servicepoller.model.Service updatedService) {
        if (!urlValidator.isValidUrl(updatedService.getUrl())) {
            System.err.println("Invalid URL provided in updated service!");
            return null;
        }

        return serviceRepository.findById(id).map(service -> {
            service.setName(updatedService.getName());
            service.setUrl(updatedService.getUrl());
            service.setStatus(updatedService.getStatus());
            service.setTimestamp(updatedService.getTimestamp());
            return serviceRepository.save(service);
        }).orElse(null);
    }

    @Override
    public boolean deleteService(Long id) {
        serviceRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteAllServicesOfUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            return false;
        }
        serviceRepository.deleteByUserId(userId);
        return true;
    }
}
