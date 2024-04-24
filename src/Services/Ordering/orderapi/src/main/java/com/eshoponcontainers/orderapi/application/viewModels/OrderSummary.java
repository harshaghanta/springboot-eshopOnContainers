package com.eshoponcontainers.orderapi.application.viewModels;

import java.util.Date;

public record OrderSummary(int ordernumber, Date date, String status, double total) {

}
