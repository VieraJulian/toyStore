package com.toyStore.orders.infra.outputport;

import com.toyStore.orders.domain.Order;

import java.util.List;

public interface IOrderRepository {

    public Order save(Order order);

    public Order getById(String id);

    public Order getByCodeOperation(String code);

    public List<Order> getAllOrders(int page, int size);

    public void delete(String id);
}
