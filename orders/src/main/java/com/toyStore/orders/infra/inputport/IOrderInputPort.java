package com.toyStore.orders.infra.inputport;

import com.toyStore.orders.application.exception.OrderNotFoundException;
import com.toyStore.orders.application.exception.ToyNotFoundException;
import com.toyStore.orders.application.exception.UpdateStockException;
import com.toyStore.orders.application.exception.UserNotFoundException;
import com.toyStore.orders.infra.dto.OrderDTO;
import com.toyStore.orders.infra.dto.OrderRequest;

import java.util.List;

public interface IOrderInputPort {

    public OrderDTO createOrder(OrderRequest orderReq) throws UserNotFoundException, ToyNotFoundException, UpdateStockException;

    public OrderDTO getOrderById(String id) throws OrderNotFoundException, UserNotFoundException, ToyNotFoundException;

    public OrderDTO getOrderByCodeOperation(String code) throws OrderNotFoundException, UserNotFoundException, ToyNotFoundException;

    public List<OrderDTO> getAllOrders(int page, int size) throws UserNotFoundException, ToyNotFoundException;

    public String deleteOrder(String id) throws OrderNotFoundException;
}
