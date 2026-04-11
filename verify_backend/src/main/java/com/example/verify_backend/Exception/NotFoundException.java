package com.example.verify_backend.Exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

public class NotFoundException extends HttpClientErrorException {

    public NotFoundException(String message) {
        super(HttpStatusCode.valueOf(404), message);
    }
}
