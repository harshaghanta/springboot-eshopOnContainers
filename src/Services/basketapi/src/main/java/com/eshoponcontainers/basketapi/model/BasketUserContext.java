package com.eshoponcontainers.basketapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketUserContext {

    private String buyerId;
    private String username;
}