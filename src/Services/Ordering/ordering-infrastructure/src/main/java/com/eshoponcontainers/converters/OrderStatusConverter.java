package com.eshoponcontainers.converters;

import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer>{

    @Override
    public Integer convertToDatabaseColumn(OrderStatus status) {
        return status.getId();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer id) {
        return OrderStatus.from(id);
    }

}
