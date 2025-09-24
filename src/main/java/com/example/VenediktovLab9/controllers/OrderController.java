package com.example.VenediktovLab9.controllers;

import com.example.VenediktovLab9.models.Order;
import com.example.VenediktovLab9.models.OrderItem;
import com.example.VenediktovLab9.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Order> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable int id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Order create(@RequestBody Map<String, Object> body) {
        Integer userId = (Integer) body.get("userId");
        List<Map<String, Object>> itemsRaw = (List<Map<String, Object>>) body.get("items");
        List<OrderItem> items = itemsRaw.stream().map(m -> {
            OrderItem oi = new OrderItem();
            oi.setProductId((Integer) m.get("productId"));
            oi.setQuantity((Integer) m.get("quantity"));
            return oi;
        }).toList();

        return service.create(userId, items);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
