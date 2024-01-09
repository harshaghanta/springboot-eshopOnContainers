package com.eshoponcontainers.orderapi.application.viewModels;

import java.time.Instant;

public record OrderSummary(int ordernumber, Instant date, String status, double total) {

}
