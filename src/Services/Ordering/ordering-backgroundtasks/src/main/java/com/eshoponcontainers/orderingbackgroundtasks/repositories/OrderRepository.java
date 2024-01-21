package com.eshoponcontainers.orderingbackgroundtasks.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${gracePeriodTime:1}")
    private Integer gracePeriodTime;

    public List<Integer> getConfirmedGracePeriodOrders() {

        String query = """
                SELECT Id FROM [ordering].[orders]
                WHERE DATEDIFF(minute, [OrderDate], GETDATE()) >= ?
                AND [OrderStatusId] = 1""";

        return jdbcTemplate.queryForList(query, Integer.class, gracePeriodTime);

    }
}
