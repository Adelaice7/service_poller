package com.rmeunier.servicepoller.service;

import com.rmeunier.servicepoller.model.Service;

public interface PollerService {
    boolean pollAllServicesAndSave();
    String pollService(String url);
    Service pollServiceAndSave(Long serviceId);
}
