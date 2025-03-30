package com.example.movies.exceptions;

import org.apache.kafka.common.protocol.types.Field;

public class CustomServiceException extends RuntimeException {

    public CustomServiceException(String mesage) {

        super(mesage);
    }


    public CustomServiceException(String mesage, Throwable cause) {

        super(mesage, cause);
    }
}
