package com.toyStore.orders.infra.outputAdapter;

import com.toyStore.orders.infra.dto.ToyDTO;
import com.toyStore.orders.infra.outputport.IToyServicePort;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "toys-srv")
public interface IToyServiceAdapter extends IToyServicePort {

    @Override
    @GetMapping("/toys/{id}")
    public ToyDTO getToy(@PathVariable Long id);

    @Override
    @PutMapping("/toys/stock/{id}/{quantityToDeduct}")
    public String updateStock(@PathVariable Long id, @PathVariable int quantityToDeduct);
}
