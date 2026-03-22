package com.eshoponcontainers.orderapi.application.viewModels;

import java.time.LocalDate;

public record OrderSummary(int ordernumber, LocalDate date, String status, double total) {

}
