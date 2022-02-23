package com.rmeunier.servicepoller.service.impl;

import com.rmeunier.servicepoller.service.RequestService;
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

    /**
     * Check if the provided URL is valid according to the regex for a URL.
     * @param url the URL to check
     * @return true if matches, false if not
     */
    public boolean isValidUrl(String url) {
        if (url == null) {
            return false;
        }

        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }

    /**
     * Checks a service for possible validity. A service can be considered valid if the Http Request returns a Response.
     * @param url the URL to check
     * @return true if valid, false if no response was received
     */
    public boolean isValidService(String url) {
        if (!isValidUrl(url)) {
            return false;
        }

        HttpResponse response = requestService.requestUrL(url);
        return response != null;
    }
}
