package com.rmeunier.servicepoller.service;

public interface PollerService {
    void pollAllServicesAndSave();
    String pollService(String url);
}
