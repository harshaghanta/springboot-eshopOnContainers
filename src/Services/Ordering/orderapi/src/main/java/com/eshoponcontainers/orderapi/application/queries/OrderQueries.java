package com.eshoponcontainers.orderapi.application.queries;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.orderapi.application.viewModels.CardType;
import com.eshoponcontainers.orderapi.application.viewModels.Order;
import com.eshoponcontainers.orderapi.application.viewModels.OrderItem;
import com.eshoponcontainers.orderapi.application.viewModels.OrderSummary;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderQueries {

    private final IOrderRepository orderRepository;
    private final EntityManager entityManager;

    public Order getOrder(int id) {
        String strQuery = "select o.[Id] as ordernumber,o.OrderDate as date, o.Description as description," +
        "o.Address_City as city, o.Address_Country as country, o.Address_State as state, o.Address_Street as street, o.Address_ZipCode as zipcode," +
        "os.Name as status, " +
        "oi.ProductName as productname, oi.Units as units, oi.UnitPrice as unitprice, oi.PictureUrl as pictureurl " +
        "FROM [ordering].[orders] o " +
        "LEFT JOIN [ordering].[orderitems] oi ON o.Id = oi.orderid " +
        "LEFT JOIN [ordering].[orderstatus] os on o.OrderStatusId = os.Id " +
        "WHERE o.Id= :orderId";
        Query query = entityManager.createNativeQuery(strQuery);
        query.setParameter("orderId", id);
        List orderData = query.getResultList();

        return mapOrderItems(orderData);
    }

    private Order mapOrderItems(List orderData) {
        Order order = new Order();

        Object[] orderCommonAttributes =  (Object[]) orderData.get(0);       

        order.setOrderNumber((Integer) orderCommonAttributes[0]);
        Timestamp timestamp = (java.sql.Timestamp)orderCommonAttributes[1];
        Instant instant = timestamp.toInstant();
        order.setDate(instant);
        order.setDescription((String) orderCommonAttributes[2]);
        order.setCity((String) orderCommonAttributes[3]);
        order.setCountry((String) orderCommonAttributes[4]);
        order.setState((String) orderCommonAttributes[5]);
        order.setStreet((String) orderCommonAttributes[6]);
        order.setZipCode((String) orderCommonAttributes[7]);
        order.setStatus((String) orderCommonAttributes[8]);
        order.setTotal(0d);        
        
        for (Object item : orderData) {
            Object[] itemDetails = (Object[])  item;
            OrderItem orderItem = new OrderItem((String) itemDetails[9], (Integer) itemDetails[10],(BigDecimal) itemDetails[11], (String) itemDetails[12]);
            order.getOrderItems().add(orderItem);
            double total = order.getTotal() + (orderItem.units() * orderItem.unitPrice().doubleValue());            
            order.setTotal(total);
        }
        return order;
    }

    public List<OrderSummary> getOrdersFromUser(UUID userId) {

        String strQuery = "SELECT o.[Id] as ordernumber,o.[OrderDate] as [date],os.[Name] as [status], SUM(oi.units*oi.unitprice) as total " +
        "FROM [ordering].[Orders] o " +
        "LEFT JOIN [ordering].[orderitems] oi ON  o.Id = oi.orderid " +
        "LEFT JOIN [ordering].[orderstatus] os on o.OrderStatusId = os.Id " +                     
        "LEFT JOIN [ordering].[buyers] ob on o.BuyerId = ob.Id " +
        "WHERE ob.IdentityGuid = :userId " +
        "GROUP BY o.[Id], o.[OrderDate], os.[Name] " +
        "ORDER BY o.[Id]";

        Query query = entityManager.createNativeQuery(strQuery);
        query.setParameter("userId", userId.toString());
        List resultList = query.getResultList();

        List<OrderSummary> orders = mapToOrderSummary(resultList);
        return orders;
    }

    private List<OrderSummary> mapToOrderSummary(List resultList) {

        List<OrderSummary> orders = new ArrayList<>();
        for (Object rowObject : resultList) {
            Object[] rowData = (Object[]) rowObject;
            OrderSummary summary = new OrderSummary((Integer) rowData[0], ((java.sql.Timestamp)rowData[1]), (String) rowData[2], ((BigDecimal) rowData[3]).doubleValue());
            orders.add(summary);
        }
        return orders;
    }

    public List<CardType> getCardTypes() {
        Query query = entityManager.createNativeQuery("SELECT Id, Name FROM [ordering].[cardtypes]", CardType.class);
        List<CardType> cardTypes = (List<CardType>) query.getResultList();
        return cardTypes;
    }



}
