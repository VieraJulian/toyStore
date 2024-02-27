package com.toyStore.orders.infra.inputportAdapter;

import com.toyStore.orders.infra.dto.OrderDTO;
import com.toyStore.orders.infra.dto.OrderRequest;
import com.toyStore.orders.infra.inputport.IOrderInputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private IOrderInputPort orderInputPort;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String id){
        try {
            OrderDTO order = orderInputPort.getOrderById(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting order", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<OrderDTO> getOrderByCode(@PathVariable String code){
        try {
            OrderDTO order = orderInputPort.getOrderByCodeOperation(code);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting order", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@RequestParam int page, @RequestParam int size){
        try {
            List<OrderDTO> orders = orderInputPort.getAllOrders(page, size);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting orders", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest or){
        try {
            OrderDTO order = orderInputPort.createOrder(or);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error creating order", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> createOrder(@PathVariable String id){
        try {
            String msj = orderInputPort.deleteOrder(id);
            return new ResponseEntity<>(msj, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting order", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}