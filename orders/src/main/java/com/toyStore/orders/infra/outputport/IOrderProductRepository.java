package com.toyStore.orders.infra.outputport;

import com.toyStore.orders.domain.Order;
import com.toyStore.orders.domain.OrderProduct;

import java.util.List;

public interface IOrderProductRepository {

    public OrderProduct save(OrderProduct orderProduct);

    public List<OrderProduct> getByOrderId(String id);

    public void delete(String id);
}
