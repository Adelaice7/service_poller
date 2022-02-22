package com.rmeunier.servicepoller.service;

import java.net.http.HttpResponse;

public interface RequestService {
    public HttpResponse requestUrL(String url);
}
