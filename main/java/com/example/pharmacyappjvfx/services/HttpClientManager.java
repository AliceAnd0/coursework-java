package com.example.pharmacyappjvfx.services;


import java.net.http.HttpClient;

public class HttpClientManager {

    private static HttpClient instance;

    private HttpClientManager() {}

    public static HttpClient getInstance() {
        if (instance == null) {
            instance = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();
        }
        return instance;
    }
}