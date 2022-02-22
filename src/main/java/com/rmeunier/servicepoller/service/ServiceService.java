package com.rmeunier.servicepoller.service;

import com.rmeunier.servicepoller.model.Service;

import java.util.List;

public interface ServiceService {
    List<Service> getAllServices();
    List<Service> getAllServicesByUserId(Long userId);
    Service getServiceById(Long id);

    Service addService(Long userId, Service service);
    Service updateService(Long id, Service updatedService);
    boolean deleteService(Long id);
    boolean deleteAllServicesOfUser(Long userId);
}
