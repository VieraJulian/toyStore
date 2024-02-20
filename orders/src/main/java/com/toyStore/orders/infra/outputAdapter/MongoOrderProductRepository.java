package com.toyStore.orders.infra.outputAdapter;

import com.toyStore.orders.domain.OrderProduct;
import com.toyStore.orders.infra.outputport.IOrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class MongoOrderProductRepository implements IOrderProductRepository {

    @Autowired
    private MongoTemplate mt;

    @Override
    public OrderProduct save(OrderProduct orderProduct) {
        return mt.save(orderProduct);
    }

    @Override
    public List<OrderProduct> getByOrderId(String id) {
        Query query = new Query(Criteria.where("order_id").is(id));
        return mt.find(query, OrderProduct.class);
    }

    @Override
    public void delete(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mt.findAndRemove(query, OrderProduct.class);
    }
}
