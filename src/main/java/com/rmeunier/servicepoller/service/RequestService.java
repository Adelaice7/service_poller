package com.rmeunier.servicepoller.service;

import java.net.http.HttpResponse;

public interface RequestService {
    HttpResponse requestUrL(String url);
}
