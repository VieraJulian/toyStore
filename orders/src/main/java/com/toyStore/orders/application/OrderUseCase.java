package com.toyStore.orders.application;

import com.toyStore.orders.application.exception.OrderNotFoundException;
import com.toyStore.orders.application.exception.ToyNotFoundException;
import com.toyStore.orders.application.exception.UpdateStockException;
import com.toyStore.orders.application.exception.UserNotFoundException;
import com.toyStore.orders.domain.Order;
import com.toyStore.orders.domain.OrderProduct;
import com.toyStore.orders.infra.dto.*;
import com.toyStore.orders.infra.inputport.IOrderInputPort;
import com.toyStore.orders.infra.outputport.IOrderProductRepository;
import com.toyStore.orders.infra.outputport.IOrderRepository;
import com.toyStore.orders.infra.outputport.IToyServicePort;
import com.toyStore.orders.infra.outputport.IUserServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderUseCase implements IOrderInputPort {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderProductRepository orderProductRepository;

    @Autowired
    private IToyServicePort toySrv;

    @Autowired
    private IUserServicePort userSrv;


    @Override
    public OrderDTO createOrder(OrderRequest orderReq) throws UserNotFoundException, ToyNotFoundException, UpdateStockException {
        UserDTO user = userSrv.getUser(orderReq.getUser_id());

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        List<ToyDTO> toys = new ArrayList<>();

        List<ProductDTO> products = new ArrayList<>();

        BigDecimal priceFinal = new BigDecimal(0);


        for (ProductRequest pr: orderReq.getProducts()) {
            ToyDTO toy = toySrv.getToy(pr.getProduct_id());

            if (toy == null) {
                throw new ToyNotFoundException("Toy not found");
            }

            priceFinal = priceFinal.add(toy.getPriceWithDiscount().multiply(BigDecimal.valueOf(pr.getQuantity())));

            String msj = toySrv.updateStock(toy.getId(), pr.getQuantity());

            if (msj == null) {
                throw new UpdateStockException("Error updating stock");
            }

            ProductDTO pDTO = ProductDTO.builder()
                    .id(toy.getId())
                    .name(toy.getName())
                    .price(toy.getPrice())
                    .discount(toy.getDiscount())
                    .priceWithDiscount(toy.getPriceWithDiscount())
                    .quantity(pr.getQuantity())
                    .build();

            toy.setQuantity(pr.getQuantity());
            products.add(pDTO);
            toys.add(toy);
        }

        Order order = Order.builder()
                .user_id(user.getId())
                .price(priceFinal)
                .date(LocalDateTime.now())
                .codeOperation(UUID.randomUUID().toString())
                .build();

        Order orderDB = orderRepository.save(order);

        for (ToyDTO toy : toys) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .order_id(orderDB.getId())
                    .product_id(toy.getId())
                    .quantity(toy.getQuantity())
                    .build();

            orderProductRepository.save(orderProduct);
        }

        return OrderDTO.builder()
                .order_id(orderDB.getId())
                .price(orderDB.getPrice())
                .date(orderDB.getDate())
                .codeOperation(orderDB.getCodeOperation())
                .user(user)
                .products(products)
                .build();
    }

    @Override
    public OrderDTO getOrderById(String id) throws OrderNotFoundException, UserNotFoundException, ToyNotFoundException {
        Order orderDB = orderRepository.getById(id);

        if (orderDB == null) {
            throw new OrderNotFoundException("Order not found");
        }

        UserDTO user = userSrv.getUser(orderDB.getUser_id());

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        List<OrderProduct> orderProducts = orderProductRepository.getByOrderId(orderDB.getId());

        List<ProductDTO> products = new ArrayList<>();

        for (OrderProduct op : orderProducts) {
            ToyDTO toy = toySrv.getToy(op.getProduct_id());

            if (toy == null) {
                throw new ToyNotFoundException("Toy not found");
            }

            ProductDTO p = ProductDTO.builder()
                    .id(toy.getId())
                    .name(toy.getName())
                    .price(toy.getPrice())
                    .discount(toy.getDiscount())
                    .priceWithDiscount(toy.getPriceWithDiscount())
                    .quantity(op.getQuantity())
                    .build();

            products.add(p);
        }

        return OrderDTO.builder()
                .order_id(orderDB.getId())
                .price(orderDB.getPrice())
                .date(orderDB.getDate())
                .user(user)
                .codeOperation(orderDB.getCodeOperation())
                .products(products)
                .build();
    }

    @Override
    public OrderDTO getOrderByCodeOperation(String code) throws OrderNotFoundException, UserNotFoundException, ToyNotFoundException {
        Order order = orderRepository.getByCodeOperation(code);

        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }

        UserDTO user = userSrv.getUser(order.getUser_id());

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        List<OrderProduct> orderProducts = orderProductRepository.getByOrderId(order.getId());

        List<ProductDTO> products = new ArrayList<>();

        for (OrderProduct op : orderProducts) {
            ToyDTO toy = toySrv.getToy(op.getProduct_id());

            if (toy == null) {
                throw new ToyNotFoundException("Toy not found");
            }

            ProductDTO p = ProductDTO.builder()
                    .id(toy.getId())
                    .name(toy.getName())
                    .price(toy.getPrice())
                    .discount(toy.getDiscount())
                    .priceWithDiscount(toy.getPriceWithDiscount())
                    .quantity(op.getQuantity())
                    .build();

            products.add(p);
        }

        return OrderDTO.builder()
                .order_id(order.getId())
                .price(order.getPrice())
                .date(order.getDate())
                .user(user)
                .codeOperation(order.getCodeOperation())
                .products(products)
                .build();

    }

    @Override
    public List<OrderDTO> getAllOrders(int page, int size) throws UserNotFoundException, ToyNotFoundException {
        List<Order> ordersDB = orderRepository.getAllOrders(page, size);
        List<OrderDTO> ordersDTO = new ArrayList<>();

        for (Order or : ordersDB) {
            UserDTO user = userSrv.getUser(or.getUser_id());

            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            List<OrderProduct> orderProducts = orderProductRepository.getByOrderId(or.getId());

            List<ProductDTO> products = new ArrayList<>();

            for (OrderProduct op : orderProducts) {
                ToyDTO toy = toySrv.getToy(op.getProduct_id());

                if (toy == null) {
                    throw new ToyNotFoundException("Toy not found");
                }

                ProductDTO p = ProductDTO.builder()
                        .id(toy.getId())
                        .name(toy.getName())
                        .price(toy.getPrice())
                        .discount(toy.getDiscount())
                        .priceWithDiscount(toy.getPriceWithDiscount())
                        .quantity(op.getQuantity())
                        .build();

                products.add(p);
            }

            OrderDTO orDTO = OrderDTO.builder()
                    .order_id(or.getId())
                    .price(or.getPrice())
                    .date(or.getDate())
                    .codeOperation(or.getCodeOperation())
                    .user(user)
                    .products(products)
                    .build();

            ordersDTO.add(orDTO);
        }

        return ordersDTO;
    }

    @Override
    public String deleteOrder(String id) throws OrderNotFoundException {
        Order order = orderRepository.getById(id);

        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }

        List<OrderProduct> orderProducts = orderProductRepository.getByOrderId(order.getId());

        for (OrderProduct op : orderProducts) {
            orderProductRepository.delete(op.getId());
        }

        orderRepository.delete(id);

        return "Order deleted success";
    }
}
