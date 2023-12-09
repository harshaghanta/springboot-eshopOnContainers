package com.eshoponcontainers.catalogapi.excep;

public class CatalogDomainException extends Exception {
    
    private static final long serialVersionUID = -8452416079590986899L;
    
    public CatalogDomainException(String message) {
        super(message);
    }

    public CatalogDomainException(String message, Exception exception) {
        super(message, exception);
    }
}
