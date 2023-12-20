package com.eshoponcontainers.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderingDomainException extends Exception {
    
    public OrderingDomainException(String message) {
        super(message);
    }

    public OrderingDomainException(String message, Exception innerException) {
        super(message, innerException);
    }
}
