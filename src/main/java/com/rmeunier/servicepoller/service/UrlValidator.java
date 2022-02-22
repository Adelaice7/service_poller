package com.rmeunier.servicepoller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a service to validate the URLs given by the user, checking if it is a valid URL,
 * then if it is a possible service.
 */
@Component
public class UrlValidator {

    @Autowired
    private RequestService requestService;

    private final String URL_REGEX =
            "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                    "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                    "([).!';/?:,][[:blank:]])?$";

    private final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public boolean isValidUrl(String url) {
        if (url == null) {
            return false;
        }

        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }

    public boolean isValidService(String url) {
        if (!isValidUrl(url)) {
            return false;
        }

        HttpResponse response = requestService.requestUrL(url);
        return response != null;
    }
}
