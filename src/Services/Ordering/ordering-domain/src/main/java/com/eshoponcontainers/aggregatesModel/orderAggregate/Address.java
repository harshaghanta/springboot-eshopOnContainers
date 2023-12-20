package com.eshoponcontainers.aggregatesModel.orderAggregate;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.eshoponcontainers.seedWork.ValueObject;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Address extends ValueObject {

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    @Override
    protected Collection<Object> getEqualityComponents() {
        
        return Stream.of( street, city, state, country, zipCode).collect(Collectors.toList());
    }

}
