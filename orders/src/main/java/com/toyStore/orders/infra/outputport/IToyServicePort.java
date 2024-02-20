package com.toyStore.orders.infra.outputport;

import com.toyStore.orders.infra.dto.ToyDTO;
import org.springframework.stereotype.Component;

public interface IToyServicePort {

        public ToyDTO getToy(Long id);

        public String updateStock(Long id, int quantityToDeduct);
}
