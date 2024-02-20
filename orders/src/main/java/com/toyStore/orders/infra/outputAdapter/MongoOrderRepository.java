package com.toyStore.orders.infra.outputAdapter;

import com.toyStore.orders.domain.Order;
import com.toyStore.orders.infra.outputport.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;

@Repository
public class MongoOrderRepository implements IOrderRepository {

    @Autowired
    private MongoTemplate mt;

    @Override
    public Order save(Order order) {
        return mt.save(order);
    }

    @Override
    public Order getById(String id) {
        return mt.findById(id, Order.class);
    }

    @Override
    public Order getByCodeOperation(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("codeOperation").is(code));
        return mt.findOne(query, Order.class);
    }

    @Override
    public List<Order> getAllOrders(int page, int size) {
        Query query = new Query();
        query.skip(size * page);
        query.limit(size);
        return mt.find(query, Order.class);
    }

    @Override
    public void delete(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mt.findAndRemove(query, Order.class);
    }
}
