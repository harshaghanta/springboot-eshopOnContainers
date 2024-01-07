package com.eshoponcontainers.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderingDomainException extends RuntimeException {
    
    public OrderingDomainException(String message) {
        super(message);
    }

    public OrderingDomainException(String message, Exception innerException) {
        super(message, innerException);
    }
}
